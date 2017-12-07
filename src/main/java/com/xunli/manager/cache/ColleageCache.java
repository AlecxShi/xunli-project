package com.xunli.manager.cache;

import com.xunli.manager.common.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shihj on 2017/12/6.
 */
@Component
public class ColleageCache
{

    private final static Logger log = LoggerFactory.getLogger(DictInfoCache.class);

    public static List<String> commonCollege = new ArrayList<>();

    public static List<String> goodCollege = new ArrayList<>();

    public void load()
    {
        String path = ColleageCache.class.getResource("/config/").getFile();
        log.info(String.format("正在加载普通大学缓存信息,文件名为[%s]",Const.COMMON_COLLEGE_FILE));
        File common = new File(path + Const.COMMON_COLLEGE_FILE);
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(common), Charset.forName("GBK"))))
        {
            String temp ;
            while((temp = reader.readLine()) != null)
            {
                commonCollege.add(temp);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        log.info(String.format("普通大学缓存信息加载完毕,文件名为[%s]",Const.COMMON_COLLEGE_FILE));
        log.info(String.format("正在加载重点大学缓存信息,文件名为[%s]",Const.GOOD_COLLEGE_FILE));
        File good = new File(path + Const.GOOD_COLLEGE_FILE);
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(good), Charset.forName("GBK"))))
        {
            String temp ;
            while((temp = reader.readLine()) != null)
            {
                goodCollege.add(temp);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        log.info(String.format("重点大学缓存信息加载完毕,文件名为[%s]",Const.GOOD_COLLEGE_FILE));
    }
}
