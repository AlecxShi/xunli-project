package com.xunli.manager.util;

import com.xunli.manager.model.DictInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shihj on 2017/7/31.
 */
public class DictInfoUtil {
    public static List<DictInfo> getByDictType(final List<DictInfo> list,String type)
    {
        if(type == null || "".equals(type))
            return list;
        List<DictInfo> result = new ArrayList<>();
        for(DictInfo d : list)
        {
            if(d.getDictType().equals(type))
                result.add(d);
        }
        return result;
    }

    public static DictInfo getByDictTypeAndDictValue(final List<DictInfo> list,String type,String value)
    {
        if(type == null || "".equals(type))
            return null;
        if(value == null || "".equals(value))
            return null;
        DictInfo dict = null;
        for(DictInfo d : list)
        {
            if(d.getDictType().equals(type) && d.getDictValue().equals(value))
            {
                dict = d;
                break;
            }
        }
        return dict;
    }

    public static List<String> getOneLineCity(final List<DictInfo> list)
    {
        List<String> res = new ArrayList<String>();
        for(DictInfo province : getByDictType(list,"Province"))
        {
            for(DictInfo city : getByDictType(list,province.getDictValue()))
            {
                if(city.getDictDesc().contains("北京") ||
                        city.getDictDesc().contains("上海") ||
                        city.getDictDesc().contains("深圳") ||
                        city.getDictDesc().contains("广州"))
                    res.add(province.getDictDesc() + "," + city.getDictDesc());
            }
        }
        return res;
    }

    public static List<String> getOneLineCityExcept(final List<DictInfo> list,String except)
    {
        List<String> res = new ArrayList<String>();
        for(DictInfo province : getByDictType(list,"Province"))
        {
            for(DictInfo city : getByDictType(list,province.getDictValue()))
            {
                if((city.getDictDesc().contains("北京") ||
                        city.getDictDesc().contains("上海") ||
                        city.getDictDesc().contains("深圳") ||
                        city.getDictDesc().contains("广州")) && (!except.contains(city.getDictDesc())))
                    res.add(province.getDictDesc() + "," + city.getDictDesc());
            }
        }
        return res;
    }

    public static List<String> getTwoLineCity(final List<DictInfo> list)
    {
        List<String> res = new ArrayList<String>();
        for(DictInfo province : getByDictType(list,"Province"))
        {
            for(DictInfo city : getByDictType(list,province.getDictValue()))
            {
                if((!city.getDictDesc().contains("北京") &&
                        !city.getDictDesc().contains("上海") &&
                        !city.getDictDesc().contains("深圳") &&
                        !city.getDictDesc().contains("广州")))
                    res.add(province.getDictDesc() + "," + city.getDictDesc());
            }
        }
        return res;
    }
}
