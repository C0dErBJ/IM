package com.AlphaZ.util.encrypt;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class DESUtil {

    private final static String DES = "DES";
    private final static String KEY = "Z!s$m#m@%";

    /**
     * Description 根据键值进行加密
     *
     * @param data
     * @param data  加密键byte数组
     * @return
     * @throws Exception
     */
    public static String encrypt(String data) {

        byte[] bt = null;
        try {
            bt = encrypt(data.getBytes(), KEY.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String strs = Base64.encodeBase64String(bt);
        return strs;
    }

    /**
     * Description 根据键值进行解密
     *
     * @param data
     * @param data  加密键byte数组
     * @return
     * @throws Exception
     */
    public static String decrypt(String data) {

        if (data == null)
            return null;

        byte[] buf = Base64.decodeBase64(data);
        byte[] bt = null;

        try {
            bt = decrypt(buf, KEY.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(bt);
    }

    /**
     * Description 根据键值进行加密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, byte[] key) {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = null;
        try {
            dks = new DESKeySpec(key);
            // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey securekey = keyFactory.generateSecret(dks);

            // Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance(DES);

            // 用密钥初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
            return cipher.doFinal(data);
        } catch (InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException | InvalidKeySpecException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;

    }


    /**
     * Description 根据键值进行解密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, byte[] key) {
        try {
            // 生成一个可信任的随机数源
            SecureRandom sr = new SecureRandom();

            // 从原始密钥数据创建DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(key);

            // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey securekey = keyFactory.generateSecret(dks);

            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance(DES);

            // 用密钥初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }
}