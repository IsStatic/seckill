package com.example.seckilldemo.controller;

import com.example.seckilldemo.pojo.User;
import com.example.seckilldemo.rebbitmq.MQSender;
import com.example.seckilldemo.vo.RespBean;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 *
 * @author 梁
 * @date 2022-03-21
 */
@RestController
@RequestMapping("/user")
@Api(value = "",tags = "")
public class UserController {

    @Autowired
    private MQSender mqSender;


    /*
    * 用户信息-测试用
    * */
    @RequestMapping("/info")
    @ResponseBody
    public RespBean info(User user){
        return RespBean.success(user);
    }
//
//
//    /*
//    * 测试MQ消息队列
//    *
//    * */
//    @RequestMapping("/mq")
//    @ResponseBody
//    public void mq(){
//        mqSender.send("hello");
//    }
//
//    @RequestMapping(value = "/mq/fanout")
//    @ResponseBody
//    public void mqFanout() {
//        mqSender.send("Hello");
//    }
//
//    @RequestMapping(value = "/mq/direct01")
//    @ResponseBody
//    public void mqDirect01() {
//        mqSender.send01("Hello Red");
//    }
//
//    @RequestMapping(value = "/mq/direct02")
//    @ResponseBody
//    public void mqDirect02() {
//        mqSender.send02("Hello Green");
//    }
//
//    @RequestMapping(value = "/mq/topic01")
//    @ResponseBody
//    public void mqtopic01() {
//        mqSender.send03("Hello Red");
//    }
//
//    @RequestMapping(value = "/mq/topic02")
//    @ResponseBody
//    public void mqtopic02() {
//        mqSender.send04("Hello Green");
//    }
//
//    @RequestMapping(value = "/mq/header01")
//    @ResponseBody
//    public void header01() {
//        mqSender.send05("Hello 01");
//    }
//
//    @RequestMapping(value = "/mq/header02", method = RequestMethod.GET)
//    @ResponseBody
//    public void header02() {
//        mqSender.send06("Hello 02");
//    }
}
