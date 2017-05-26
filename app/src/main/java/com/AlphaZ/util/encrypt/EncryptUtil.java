package com.AlphaZ.util.encrypt;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * ProjectName: YouChi
 * PackageName: com.YC.com.AlphaZ.util
 * User: C0dEr
 * Date: 2016-11-09
 * Time: 11:03
 * Description:
 */
public class EncryptUtil {
    public static String EncoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5 = null;

        md5 = MessageDigest.getInstance("MD5");
        //加密后的字符串
        return Base64.getEncoder().encodeToString(md5.digest(str.getBytes("utf-8")));


    }
}