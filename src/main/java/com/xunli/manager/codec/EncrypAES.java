package com.xunli.manager.codec;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.xunli.manager.util.RandomUtil;
import org.apache.commons.codec.binary.Base64;

/**
 * Created by shihj on 2017/9/26.
 */
public class EncrypAES {
    //密钥的KEY
    private static final String KEY = "XL@2020#^)*(&&(*";
    //KeyGenerator 提供对称密钥生成器的功能，支持各种算法
    private KeyGenerator keygen;
    //SecretKey 负责保存对称密钥
    private SecretKey deskey;
    //Cipher负责完成加密或解密工作
    private Cipher c;
    //该字节数组负责保存加密的结果
    private byte[] cipherByte;

    public EncrypAES() throws NoSuchAlgorithmException, NoSuchPaddingException
    {
        //Security.addProvider(new com.sun.crypto.provider.SunJCE());
        //实例化支持DES算法的密钥生成器(算法名称命名需按规定，否则抛出异常)
        //keygen = KeyGenerator.getInstance("AES");
        //生成密钥
        //deskey = keygen.generateKey();
        deskey = new SecretKeySpec(KEY.getBytes(), "AES");
        //生成Cipher对象,指定其支持的DES算法
        c = Cipher.getInstance("AES");
    }

    /**
     * 对字符串加密
     * @param str
     * @return
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public String Encrytor(String str) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        // 根据密钥，对Cipher对象进行初始化，ENCRYPT_MODE表示加密模式
        c.init(Cipher.ENCRYPT_MODE, deskey);
        byte[] src = str.getBytes();
        // 加密，结果保存进cipherByte
        cipherByte = c.doFinal(src);
        return Base64.encodeBase64String(cipherByte);
    }

    /**
     * 对字符串解密
     * @param str
     * @return
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public String Decryptor(String str) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        // 根据密钥，对Cipher对象进行初始化，DECRYPT_MODE表示加密模式
        c.init(Cipher.DECRYPT_MODE, deskey);
        byte[] bytes = Base64.decodeBase64(str.getBytes());
        cipherByte = c.doFinal(bytes);
        return new String(cipherByte);
    }

    /**
     * @param args
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     */
    /*public static void main(String[] args) throws Exception
    {
        EncrypAES de1 = new EncrypAES();
        String msg = RandomUtil.generatePassword();
        long l1 = System.currentTimeMillis();
        String code = de1.Encrytor(msg);
        long l2 = System.currentTimeMillis();
        String decontent = de1.Decryptor(code);
        long l3 = System.currentTimeMillis();
        System.out.println("明文是:" + msg);
        System.out.println("加密后:" + code + ",spend[" + (l2 - l1) + "]");
        System.out.println("解密后:" + decontent + ",spend[" + (l3 - l2) + "]");
    }*/
}
