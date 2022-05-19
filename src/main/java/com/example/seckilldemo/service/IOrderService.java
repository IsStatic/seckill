package com.example.seckilldemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckilldemo.pojo.Order;
import com.example.seckilldemo.pojo.User;
import com.example.seckilldemo.vo.GoodsVo;
import com.example.seckilldemo.vo.OrderDetailVo;

/**
 * 
 *
 * @author 梁
 * @date 2022-03-21
 */
public interface IOrderService extends IService<Order> {

    /*
    * 秒杀
    * */
    Order seckill(User user, GoodsVo goodsVo);

    /*
    * 订单详情
    * */
    OrderDetailVo detail(Long orderId);

    /*
    * 获取秒杀地址
    * */
    String createPath(User user, Long goodsId);

    /*
    * 校验秒杀地址
    * */
    Boolean checkPath(User user, Long goodsId, String path);



    /*
     * 校验验证码
     * */
    boolean checkCaptcha(User user, Long goodsId, String captcha);
}
