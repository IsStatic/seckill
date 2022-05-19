package com.example.seckilldemo.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public class MD5Utils {
    public static String md5(String str){
        return DigestUtils.md5Hex(str);
    }
    private static final String slat="1a2b3c4d";

    public static String inputPassToFromPass(String inputPass){
        String str = "" +slat.charAt(0) + slat.charAt(2) + inputPass +
                slat.charAt(5) + slat.charAt(4);
        return md5(str);
    }

    public static String fromPassToDBpass(String frompass,String slat){
        String str = slat.charAt(0) + frompass + slat.charAt(2);
        return md5(str);
    }

    public static String inputPassToDBPass(String inputpass , String slat){
        String frompass = inputPassToFromPass(inputpass);
        String Dbpass = fromPassToDBpass(frompass,slat);
        return Dbpass;
    }

    public static void main(String[] args) {
        //d3b1294a61a07da9b49b6e22b2cbd7f9
        System.out.println(inputPassToFromPass("123456"));
        System.out.println(fromPassToDBpass("d3b1294a61a07da9b49b6e22b2cbd7f9","4235abc"));
        System.out.println(inputPassToDBPass("123456","1a2b3c4d"));
    }
}
