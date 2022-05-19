package com.example.seckilldemo.controller;

import com.example.seckilldemo.pojo.User;
import com.example.seckilldemo.service.IOrderService;
import com.example.seckilldemo.vo.OrderDetailVo;
import com.example.seckilldemo.vo.RespBean;
import com.example.seckilldemo.vo.RespBeanEnum;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 *
 * @author 梁
 * @date 2022-03-21
 */
@RestController
@RequestMapping("/order")
@Api(value = "",tags = "")
public class OrderController {

    @Autowired
    private IOrderService orderService;


    /*
    * 订单详情
    *
    * */
    @RequestMapping("/detail")
    @ResponseBody
    public RespBean detail(User user,Long orderId){
        if(user==null){
            return RespBean.error(RespBeanEnum.SESEION_ERROR);
        }
        OrderDetailVo detail = orderService.detail(orderId);
        return RespBean.success(detail);
    }
}
