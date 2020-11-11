package com.flow.demo.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * 随机盐工具类
 */
@Component
public class SaltUtils {
    public static  String getSalt(int n){
        StringBuilder stringBuilder=new StringBuilder();
        char[] chars="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz012456789*#%$#@".toCharArray();
        for (int i=0;i<n;i++){
            char achar=chars[new Random().nextInt(chars.length)];
            stringBuilder.append(achar);
        }
    return stringBuilder.toString();
    }

}
