package com.xunli.manager.util;

import com.xunli.manager.common.Const;
import com.xunli.manager.config.Constants;

import java.io.File;

/**
 * Created by shihj on 2017/9/13.
 */
public class ImageUtil {

    public static String getAllPathByUserId(Long userId)
    {
        File path = new File(Constants.IMAGE_ROOT_DIR + userId);
        StringBuffer sb = new StringBuffer();
        if(path.isDirectory())
        {
            File[] files = path.listFiles();
            for(File file : files)
            {
                sb.append(String.format(Constants.HTTP_IMAGE_PATH,userId,file.getName())).append(Constants.SPERATOR);
            }
            if(sb.length() != 0 && sb.lastIndexOf(Constants.SPERATOR) == (sb.length() - 1))
            {
                sb.substring(0,sb.length() - 1);
            }
        }
        return sb.toString();
    }

    public static String getUserIconByUserId(Long userId)
    {
        //取私人头像
        File path = new File(Constants.ICON_ROOT_DIR);
        StringBuffer sb = new StringBuffer();
        if(path.isDirectory())
        {
            File[] files = path.listFiles();
            for(File file : files)
            {
                if(file.getName().contains(userId + "_icon"));
                {
                    sb.append(String.format(Constants.HTTP_ICON_PATH,file.getName()));
                    break;
                }
            }
        }
        //取公共头像
        if(sb.length() == 0)
        {
            path = new File(Constants.ICON_PUBLIC_ROOT_DIR);
            if(path.isDirectory())
            {
                File[] files = path.listFiles();
                if(files.length >= 0)
                {
                    sb.append(String.format(Constants.HTTP_ICON_PATH,files[(int)(Math.random() * (files.length - 1))].getName()));
                }
            }
        }
        return sb.toString();
    }

    public static String getDefaultIcon(Long gender)
    {
        if(DictInfoUtil.isMale(gender))
            return Const.DEFAULT_MALE_ICON;
        else
            return Const.DEFAULT_FEMALE_ICON;
    }
}
