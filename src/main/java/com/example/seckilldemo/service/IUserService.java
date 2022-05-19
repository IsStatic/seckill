package com.example.seckilldemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckilldemo.pojo.User;
import com.example.seckilldemo.vo.Loginvo;
import com.example.seckilldemo.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 *服务类
 * @author 梁
 * @date 2022-03-21
 */
public interface IUserService extends IService<User> {

    /*
    * 登录接口
    * */
    RespBean doLogin(Loginvo vo, HttpServletRequest request, HttpServletResponse response);

    /*
    * 根据cookie获取用户
    * */

    User getUserByCookie(String userTicket,HttpServletRequest request,HttpServletResponse response);

    /*
    * 更新密码
    * */
    RespBean updatePassword(String userTicket, String password, HttpServletRequest request,
                            HttpServletResponse response);
}
