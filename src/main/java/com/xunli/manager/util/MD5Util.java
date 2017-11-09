package com.xunli.manager.util;

import java.security.MessageDigest;

/**
 * Created by shihj on 2017/9/27.
 */
public class MD5Util {

    /**
     * MD5简单加密算法
     * @param obj
     * @return
     */
    public  static  String Encode(Object obj)
    {
        String tar = String.valueOf(obj);
        MessageDigest md5;
        try
        {
            md5 = MessageDigest.getInstance("MD5");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
        byte[] md5Bytes = md5.digest(tar.getBytes());
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++)
        {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
            {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}
