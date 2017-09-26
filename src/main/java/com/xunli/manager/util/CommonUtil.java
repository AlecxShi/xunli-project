package com.xunli.manager.util;

import com.xunli.manager.codec.EncrypAES;
import com.xunli.manager.model.CommonUser;

/**
 * Created by shihj on 2017/9/26.
 */
public class CommonUtil {

    public static CommonUser encrypPassword(CommonUser user)
    {
        try
        {
            EncrypAES encrypAES = new EncrypAES();
            user.setPassword(user.getPassword() == null || "".equals(user.getPassword()) ?
                    encrypAES.Encrytor(RandomUtil.generatePassword()) :
                    encrypAES.Encrytor(user.getPassword()));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return user;
    }
}
