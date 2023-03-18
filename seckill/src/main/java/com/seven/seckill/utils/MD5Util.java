package com.seven.seckill.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MD5Util {

    public static String md5(String src){
        //md5加密
        return DigestUtils.md5Hex(src);
    }

    //盐值，前端第一次加密使用
    private static final String salt="1a2b3c4d";
    //第一次加密
    public static String inputPassToFromPass(String inputPass){
        String str = salt.charAt(0)+salt.charAt(0)+inputPass+salt.charAt(0)+salt.charAt(0);
        return md5(str);
    }

    //第二次加密
    public static String fromPassToDBPass(String fromPass, String salt){
        String str = salt.charAt(0)+salt.charAt(0)+fromPass+salt.charAt(0)+salt.charAt(0);
        return md5(str);
    }

    //从输入密码到数据库存储密码
    public static String inputPassToDBPass(String inputPass,String salt){
        return fromPassToDBPass(inputPassToFromPass(inputPass),salt);
    }
}
