package com.example.seckilldemo.controller;


import com.example.seckilldemo.service.IUserService;
import com.example.seckilldemo.vo.Loginvo;
import com.example.seckilldemo.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Autowired
    private IUserService userService;
    /*
    * 跳转登录页面
    * */
    @GetMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    /*
    * 登录功能
    * */
    // HttpServletResponse 封装HTTP响应信息 HttpServletRequest 封装HTTP请求信息
    @PostMapping("/doLogin")
    @ResponseBody
    public RespBean doLogin(@Valid Loginvo vo , HttpServletResponse response, HttpServletRequest request){
        return userService.doLogin(vo,request,response);
    }

}
