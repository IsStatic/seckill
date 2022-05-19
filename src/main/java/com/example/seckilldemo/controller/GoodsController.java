package com.example.seckilldemo.controller;

import com.example.seckilldemo.pojo.User;
import com.example.seckilldemo.service.IGoodsService;
import com.example.seckilldemo.service.IUserService;
import com.example.seckilldemo.vo.DetailVo;
import com.example.seckilldemo.vo.GoodsVo;
import com.example.seckilldemo.vo.RespBean;
import io.swagger.annotations.Api;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.spring5.view.reactive.ThymeleafReactiveView;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.WebConnection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 
 *商品
 * @author 梁
 * @date 2022-03-21
 */
@Controller
@RequestMapping("/goods")
@Api(value = "",tags = "")
public class GoodsController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;


    /*
    * 跳转到商品页表
    * 优化前QPS：223.9/sec
    *缓存游湖qps:631.9/sec
    *
    *
    * */
    @RequestMapping(value = "/toList",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String tiList(Model model , User user, HttpServletRequest request, HttpServletResponse response){
        //Redis中获取页面，如果不为空直接返回页面
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsList");
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        //        if(StringUtils.isEmpty(ticket)){
//            return "login";
//        }
//
////      User user = (User) session.getAttribute(ticket);
//        User user = userService.getUserByCookie(ticket, request, response);
//        if(user == null) return "login";
        model.addAttribute("user", user);
        model.addAttribute("goodsList",goodsService.findGoodsVo());
        //如果为空，手动渲染，存入redis，并放回
        WebContext webContext = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList", webContext);
        if(!StringUtils.isEmpty(html)){
            valueOperations.set("goodList",html,60, TimeUnit.SECONDS);
        }
        return html;
    }

    /*
    * 跳转到商品详情页
    * */
    @RequestMapping(value = "/Detail2/{goodsId}",produces ="text/html;charset=utf-8" )
    @ResponseBody
    public String toDetail2(Model model,User user, @PathVariable Long goodsId,HttpServletRequest request, HttpServletResponse response){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsDetail" + goodsId);
        //Redis中获取页面，如果不为空直接返回页面
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        model.addAttribute("user", user);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();
         //秒杀状态
        int secKillStatus = 0;
        int remainSeconds = 0;
        if(nowDate.before(startDate)){
            //秒杀还没开始
            remainSeconds = ((int) ((startDate.getTime() - nowDate.getTime()) / 1000));
        }else if(nowDate.after(endDate)){
            //秒杀已结束
            secKillStatus=2;
            remainSeconds = -1;
        }else {
            //秒杀进行中
            secKillStatus=1;
            remainSeconds = 0;
        }
        model.addAttribute("remainSeconds",remainSeconds);
        model.addAttribute("secKillStatus",secKillStatus);
        model.addAttribute("goods",goodsVo);
//        return "goodsDetail";
        //如果为空，手动渲染，存入redis，并放回
        WebContext webContext = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail", webContext);
        if(!StringUtils.isEmpty(html)){
            valueOperations.set("goodsDetail"+goodsId,html,60, TimeUnit.SECONDS);
        }
        return html;
    }



    /*
    * 跳转到商品详情页
    *
    * */
    @RequestMapping(value = "/Detail/{goodsId}" )
    @ResponseBody
    public RespBean toDetail(Model model,User user, @PathVariable Long goodsId){
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();
        //秒杀状态
        int secKillStatus = 0;
        //秒杀倒计时
        int remainSeconds = 0;
        //秒杀还未开始
        if (nowDate.before(startDate)) {
            remainSeconds = ((int) ((startDate.getTime() - nowDate.getTime()) / 1000));
        } else if (nowDate.after(endDate)) {
            //	秒杀已结束
            secKillStatus = 2;
            remainSeconds = -1;
        } else {
            //秒杀中
            secKillStatus = 1;
            remainSeconds = 0;
        }
        DetailVo detailVo = new DetailVo();
        detailVo.setUser(user);
        detailVo.setGoodsVo(goodsVo);
        detailVo.setSecKillStatus(secKillStatus);
        detailVo.setRemainSeconds(remainSeconds);
        return RespBean.success(detailVo);
    }
}
