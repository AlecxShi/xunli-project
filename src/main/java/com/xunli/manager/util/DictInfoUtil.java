package com.xunli.manager.util;

import com.xunli.manager.cache.DictInfoCache;
import com.xunli.manager.model.DictInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shihj on 2017/7/31.
 */
public class DictInfoUtil {
    public static List<DictInfo> getByDictType(String type)
    {
        if(type == null || "".equals(type))
            return DictInfoCache.dictInfos;
        List<DictInfo> result = new ArrayList<>();
        for(DictInfo d : DictInfoCache.dictInfos)
        {
            if(d.getDictType().equals(type))
                result.add(d);
        }
        return result;
    }

    public static DictInfo getByDictTypeAndDictValue(String type,String value)
    {
        if(type == null || "".equals(type))
            return null;
        if(value == null || "".equals(value))
            return null;
        DictInfo dict = null;
        for(DictInfo d : DictInfoCache.dictInfos)
        {
            if(d.getDictType().equals(type) && d.getDictValue().equals(value))
            {
                dict = d;
                break;
            }
        }
        return dict;
    }

    public static List<String> getOneLineCity()
    {
        List<String> res = new ArrayList<String>();
        for(DictInfo province : getByDictType("Province"))
        {
            for(DictInfo city : getByDictType(province.getDictValue()))
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

    public static List<String> getOneLineCityExcept(String except)
    {
        List<String> res = new ArrayList<String>();
        for(DictInfo province : getByDictType("Province"))
        {
            for(DictInfo city : getByDictType(province.getDictValue()))
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

    public static List<String> getTwoLineCity()
    {
        List<String> res = new ArrayList<String>();
        for(DictInfo province : getByDictType("Province"))
        {
            for(DictInfo city : getByDictType(province.getDictValue()))
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

    public static DictInfo getItemById(Long id)
    {
        DictInfo ret = null;
        for(DictInfo dict : DictInfoCache.dictInfos)
        {
            if(id.equals(dict.getId()))
            {
                ret =  dict;
                break;
            }
        }
        return ret;
    }

    public static DictInfo getOppositeSex(DictInfo sex)
    {
        List<DictInfo> gender = getByDictType("Gender");
        String sexKey = sex.getDictValue().equals("Male") ? "Female" : "Male";
        for(DictInfo d : gender)
        {
            if(d.getDictValue().equals(sexKey))
                return d;
        }
        return null;
    }

    public static List<Long> getBiggerEducation(Long current)
    {
        List<Long> list = new ArrayList();
        List<DictInfo> educations = getByDictType("Education");
        for(DictInfo education : educations)
        {
            Long value = Long.parseLong(education.getDictValue());
            if(value.longValue() > current.longValue())
            {
                list.add(value);
            }
        }
        return list;
    }
}
