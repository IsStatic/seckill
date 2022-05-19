package com.example.seckilldemo.config;


import com.example.seckilldemo.pojo.User;

/*
*
*
* */
public class UserContext {
    private static ThreadLocal<User> userHoder = new ThreadLocal<User>();


    public static void setUser(User user){
        userHoder.set(user);
    }

    public static User getUser(){
        return userHoder.get();
    }

}
