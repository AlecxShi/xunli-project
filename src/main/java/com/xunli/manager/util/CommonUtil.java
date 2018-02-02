package com.xunli.manager.util;

import com.xunli.manager.codec.EncrypAES;
import com.xunli.manager.model.CommonUser;

import java.util.Random;

/**
 * Created by shihj on 2017/9/26.
 */
public class CommonUtil {

    private final static Random rand = new Random();

    public static CommonUser encrypPassword(CommonUser user)
    {
        try
        {
            EncrypAES encrypAES = new EncrypAES();
            if(user.getPassword() == null || "".equals(user.getPassword()) || user.getPassword().length() > 32)
            {
                String pass = encrypAES.Encrytor(RandomUtil.generatePassword());
                if(pass.length() > 32)
                {
                    if(user.getPhone() != null)
                    {
                        user.setPassword(encrypAES.Encrytor(user.getPhone()));
                    }
                    else
                    {
                        user.setPassword(encrypAES.Encrytor(String.valueOf(user.getId())));
                    }
                }
                else
                {
                    user.setPassword(pass);
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return user;
    }


    public static int randGetBetween(int start,int end)
    {
        int result = end;
        if(start < end)
        {
            result = rand.nextInt(end);
            while(start > result)
            {
                result = rand.nextInt(end);
            }
        }
        return result;
    }
}
