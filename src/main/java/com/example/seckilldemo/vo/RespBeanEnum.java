package com.example.seckilldemo.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/*
*
* 公共返回对象枚举
* */
@ToString
@Getter
@AllArgsConstructor
public enum RespBeanEnum {
    //通用
    SUCCESS(200,"SUCCESS"),
    ERROR(501,"服务器ERROR"),
    //登录模块
    LOGIN_ERROR(114514,"用户名或密码错误"),
    MOBILE_ERROR(114513,"手机号码不正确"),
    BIND_ERROR(114512,"参数校验异常"),
    MOBILE_NOT_EXIST(114511,"用户不存在"),
    PASSWORD_UPDATE_FAIL(114510,"更新密码错误"),
    SESEION_ERROR(114490,"用户不存在"),

    //秒杀模块
    EMPTY_STOCK(996,"没有库存"),
    REPEATE_ERROR(997,"该商品限购一件"),
    REQUEST_TLLEGAL(998,"请求非法，请重新尝试"),
    ERROR_CARTCHA(999,"验证码不正确,请重新输入"),
    ACCESS_LIMIT_REAHCED(995,"访问过与频繁，请稍后再试"),

    //订单模块777xx
    ORDER_NOT_EXIST(77701,"订单信息不存在"),
    QUEUE_KEEP(77702,"检测到你的秒杀正在等待中，请等待")
    ;


    private final Integer code;
    private final String message;
}
