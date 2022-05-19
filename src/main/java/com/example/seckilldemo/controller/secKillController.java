package com.example.seckilldemo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.seckilldemo.config.AccessLimit;
import com.example.seckilldemo.exception.GlobalException;
import com.example.seckilldemo.pojo.Order;
import com.example.seckilldemo.pojo.SecKillMessage;
import com.example.seckilldemo.pojo.SeckillOrder;
import com.example.seckilldemo.pojo.User;
import com.example.seckilldemo.rebbitmq.MQSender;
import com.example.seckilldemo.service.IGoodsService;
import com.example.seckilldemo.service.IOrderService;
import com.example.seckilldemo.service.ISeckillOrderService;
import com.example.seckilldemo.utils.JsonUtil;
import com.example.seckilldemo.vo.GoodsVo;
import com.example.seckilldemo.vo.RespBean;
import com.example.seckilldemo.vo.RespBeanEnum;
import com.rabbitmq.tools.json.JSONUtil;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/*
* 秒杀
* 优化前qps:483.5/ sec
*缓存优化：qps:1380
*
* */
@Slf4j
@Controller
@RequestMapping("/seckill")
public class secKillController implements InitializingBean {
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MQSender mqSender;
//    @Autowired
//    private RedisScript<Long> redisScript;


    private Map<Long,Boolean> EmptyStockMap = new HashMap<>();
    /*
    * 秒杀
    * */
    @RequestMapping("/doSeckill2")
    public String doSeckill2(Model model, User user, Long goodsId){
        System.out.println("user2 " + user);
        if(user == null){
            return "login";
        }
        model.addAttribute("user",user);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        if(goodsVo.getStockCount()<1){
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return "secKillFail";
        }
        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
        if(seckillOrder!=null){
            model.addAttribute("errmsg",RespBeanEnum.REPEATE_ERROR.getMessage());
            return "secKillFail";
        }
        Order order = orderService.seckill(user,goodsVo);
        model.addAttribute("order",order);
        model.addAttribute("goods",goodsVo);
        return "orderDetail";
    }



    @RequestMapping(value = "/{path}/doSeckill",method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSeckill(@PathVariable String path, User user, Long goodsId){
        if(user == null){
            return RespBean.error(RespBeanEnum.SESEION_ERROR);
        }
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Boolean check = orderService.checkPath(user,goodsId,path);
        if(!check){
            return RespBean.error(RespBeanEnum.REQUEST_TLLEGAL);
        }
        //判断是否重复抢购
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if(seckillOrder!=null){
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }

        //通过内存标记减少redis的访问
        if (EmptyStockMap.get(goodsId)){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }



        //保证数据库和redis的数据的统一性
        Object bj = redisTemplate.opsForValue().get("userSecKillOrder:" + user.getId() + ":" + goodsId);
        if(bj!=null){
            return RespBean.error(RespBeanEnum.QUEUE_KEEP);
        }
        redisTemplate.opsForValue().set("userSecKillOrder:" + user.getId() + ":" + goodsId, "1",10,TimeUnit.SECONDS);





        //预减库存，decrement是原子性的，且操作完后减一
        Long decrement = valueOperations.decrement("seckillGoods:" + goodsId);
//        Long decrement = (Long) redisTemplate.execute(redisScript, Collections.singletonList("seckillGoods:" + goodsId), Collections.EMPTY_LIST);

        if(decrement < 0){
            EmptyStockMap.put(goodsId,true);
            valueOperations.increment("seckillGoods:" + goodsId);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        SecKillMessage secKillMessage = new SecKillMessage(user, goodsId);
//        mqSender.senderSecKillMessage(JsonUtil.object2JsonStr(secKillMessage));
        mqSender.senderSecKillMessage(JsonUtil.object2JsonStr(secKillMessage));

        return RespBean.success(0);






/*        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        if(goodsVo.getStockCount()<1){
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

//        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));

        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if(seckillOrder!=null){
            model.addAttribute("errmsg",RespBeanEnum.REPEATE_ERROR.getMessage());
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        Order order = orderService.seckill(user,goodsVo);
        return RespBean.success(order);*/

    }
    /*
    * 获取秒杀地址
    *
    * */
    @AccessLimit(second=5,maxCount=5,needLogin=true)
    @RequestMapping(value = "/path",method = RequestMethod.GET)
    @ResponseBody
    public RespBean getPath(User user, Long goodsId, String captcha, HttpServletRequest request){
        if (user==null){
            return RespBean.error(RespBeanEnum.SESEION_ERROR);
        }
        ValueOperations valueOperations = redisTemplate.opsForValue();
//        限制访问次数,5秒内访问5次
//        StringBuffer url = request.getRequestURL();
//        captcha="0";
//        Integer count = (Integer) valueOperations.get(url + ":" + user.getId());
//        if(count==null){
//            valueOperations.set(url + ":" + user.getId(),1,5,TimeUnit.SECONDS);
//        }else if(count<5){
//            valueOperations.increment(url + ":" + user.getId());
//        }else {
//            return RespBean.error(RespBeanEnum.ACCESS_LIMIT_REAHCED);
//        }
//


        //校验验证码
        boolean check = orderService.checkCaptcha(user,goodsId,captcha);
        if(!check){
            return RespBean.error(RespBeanEnum.ERROR_CARTCHA);
        }
        //获取
        String str = orderService.createPath(user,goodsId);
        return RespBean.success(str);
    }



    /*
    *
    * 验证码
    * */
    @RequestMapping(value = "/captcha",method = RequestMethod.GET)
    public void verifyCode(User user, Long goodsId, HttpServletResponse response){
        if(user==null||goodsId<0){
            throw new GlobalException(RespBeanEnum.REQUEST_TLLEGAL);
        }
        //设置请求头为输出图片类型
        response.setContentType("image/jpg");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        //生成验证码
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130,32,3);
        redisTemplate.opsForValue().set("captcha:"+user.getId()+":"+goodsId,captcha.text(),120, TimeUnit.SECONDS);
        try {
            captcha.out(response.getOutputStream());
        } catch (IOException e) {
            log.error("验证码生成失败",e.getMessage());
        }
    }










    /*
    * 系统初始化，加载商品库存到redis
    *
    * */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list = goodsService.findGoodsVo();
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        list.forEach(goodsVo -> {
            redisTemplate.opsForValue().set("seckillGoods:"+goodsVo.getId(),goodsVo.getStockCount());
            EmptyStockMap.put(goodsVo.getId(),false);
        });
    }

    /*
    * 获取秒杀结果
    *
    * */
    @RequestMapping(value = "/result",method = RequestMethod.GET)
    @ResponseBody
    public RespBean getResult(User user,Long goodsId){
        if (user==null){
            return RespBean.error(RespBeanEnum.SESEION_ERROR);
        }
        Long OrderId = seckillOrderService.getResult(user,goodsId);
        return RespBean.success(OrderId);
    }

}
