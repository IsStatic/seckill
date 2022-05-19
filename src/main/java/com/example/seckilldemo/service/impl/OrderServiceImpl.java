package com.example.seckilldemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.seckilldemo.exception.GlobalException;
import com.example.seckilldemo.mapper.OrderMapper;
import com.example.seckilldemo.pojo.Order;
import com.example.seckilldemo.pojo.SeckillGoods;
import com.example.seckilldemo.pojo.SeckillOrder;
import com.example.seckilldemo.pojo.User;
import com.example.seckilldemo.service.IGoodsService;
import com.example.seckilldemo.service.IOrderService;
import com.example.seckilldemo.service.ISeckillGoodsService;
import com.example.seckilldemo.service.ISeckillOrderService;
import com.example.seckilldemo.utils.MD5Utils;
import com.example.seckilldemo.utils.UUIDUtil;
import com.example.seckilldemo.vo.GoodsVo;
import com.example.seckilldemo.vo.OrderDetailVo;
import com.example.seckilldemo.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 
 *
 * @author 梁
 * @date 2022-03-21
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {


    @Autowired
    private ISeckillGoodsService seckillGoodsService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;


    /*
    * 实现秒杀
    * */
    @Transactional
    @Override
    public Order seckill(User user, GoodsVo goodsVo) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //秒杀商品库存减一
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id", goodsVo.getId()));
        seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
//        seckillGoodsService.updateById(seckillGoods);
        boolean update = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().setSql("stock_count = "+"stock_count-1").eq("goods_id", goodsVo.getId()).gt("stock_count", 0));

        if(seckillGoods.getStockCount()<1){
            valueOperations.set("isStockEmpty:"+goodsVo.getId(),"0");
            return null;
        }
        //生成订单
        Order order = new Order();
        order.setGoodsId(goodsVo.getId());
        order.setUserId(user.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goodsVo.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);
        //生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goodsVo.getId());
        seckillOrder.setUserId(user.getId());
        seckillOrderService.save(seckillOrder);
        redisTemplate.opsForValue().set("order:"+ user.getId()+":"+goodsVo.getId(),seckillOrder,60*24, TimeUnit.MINUTES);
        return order;
    }

    /*
    *
    * 订单详情
    * */
    @Override
    public OrderDetailVo detail(Long orderId) {
        if(orderId==null) {
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }
        Order order = orderMapper.selectById(orderId);
        GoodsVo goodsVoByGoodsId = goodsService.findGoodsVoByGoodsId(order.getGoodsId());
        OrderDetailVo datail = new OrderDetailVo();
        datail.setOrder(order);
        datail.setGoodsVo(goodsVoByGoodsId);
        return datail;
    }

    /*
    *
    * 获取秒杀地址
    * */
    @Override
    public String createPath(User user, Long goodsId) {
        String str = MD5Utils.md5(UUIDUtil.uuid() + "12345");
        redisTemplate.opsForValue().set("seckillPath:"+user.getId()+":"+goodsId,str,60,TimeUnit.SECONDS);
        return str;
    }

    /*
    *
    * 校验秒杀地址
    *
    * */
    @Override
    public Boolean checkPath(User user, Long goodsId, String path) {
        if(user==null || goodsId<0|| StringUtils.isEmpty(path)){
            return false;
        }
        String redisPath = (String) redisTemplate.opsForValue().get("seckillPath:" + user.getId() + ":" + goodsId);
        return path.equals(redisPath);
    }

    /*
    * 校验验证码
    * */
    @Override
    public boolean checkCaptcha(User user, Long goodsId, String captcha) {
        if(StringUtils.isEmpty(captcha) || user==null || goodsId<0){
            return false;
        }
        String redisCaptcha = (String) redisTemplate.opsForValue().get("captcha:"+user.getId()+":"+goodsId);

        return captcha.equals(redisCaptcha);

    }
}
