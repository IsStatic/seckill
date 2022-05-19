package com.example.seckilldemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckilldemo.pojo.SeckillOrder;
import com.example.seckilldemo.pojo.User;

/**
 * 
 *
 * @author 梁
 * @date 2022-03-21
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {

    /*
    * 获取秒杀结果
    *
    * */
    Long getResult(User user, Long goodsId);
}
