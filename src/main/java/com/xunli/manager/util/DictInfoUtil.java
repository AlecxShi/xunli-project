package com.xunli.manager.util;

import com.xunli.manager.cache.DictInfoCache;
import com.xunli.manager.model.DictInfo;
import com.xunli.manager.model.app.TagString;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        if(type == null || "".equals(type) || value == null || "".equals(value))
            return null;
        DictInfo dict = null;
        for(DictInfo d : DictInfoCache.dictInfos)
        {
            if(type.equals(d.getDictType()) && value.equals(d.getDictValue()))
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
                    res.add(province.getDictDesc() + "-" + city.getDictDesc());
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
                    res.add(province.getDictDesc() + "-" + city.getDictDesc());
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
                    res.add(province.getDictDesc() + "-" + city.getDictDesc());
            }
        }
        return res;
    }

    public static DictInfo getItemById(Long id)
    {
        DictInfo ret = null;
        for(DictInfo dict : DictInfoCache.dictInfos)
        {
            if(dict.getId().equals(id))
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
        DictInfo currentEducation = getItemById(current);
        Long currentValue = Long.parseLong(currentEducation.getDictValue());
        for(DictInfo education : educations)
        {
            Long value = Long.parseLong(education.getDictValue());
            if(value.longValue() >= currentValue.longValue())
            {
                list.add(education.getId());
            }
        }
        return list;
    }

    public static List<Object> autoAssembleLabelColor(String[] labels)
    {
        List<Object> tags = new ArrayList<>();
        if(labels != null)
        {
            for(String label : labels)
            {
                TagString tag = new TagString();
                tag.setText(label);
                switch(label){
                    case "公务员":
                        tag.setRgbValue(getByDictTypeAndDictValue("LabelColor","1").getDictDesc());
                        break;
                    case "多金优质":
                        tag.setRgbValue(getByDictTypeAndDictValue("LabelColor","2").getDictDesc());
                        break;
                    case "坐拥多套房产":
                        tag.setRgbValue(getByDictTypeAndDictValue("LabelColor","3").getDictDesc());
                        break;
                    case "多才多艺":
                        tag.setRgbValue(getByDictTypeAndDictValue("LabelColor","4").getDictDesc());
                        break;
                    case "海外名企":
                        tag.setRgbValue(getByDictTypeAndDictValue("LabelColor","5").getDictDesc());
                        break;
                    default:
                        tag.setRgbValue(getByDictTypeAndDictValue("LabelColor","0").getDictDesc());
                        break;
                }
                tags.add(tag);
            }
        }
        return tags;
    }

    public static List<DictInfo> getRegionsFromCitys(List<DictInfo> states) {
        List<DictInfo> list = new ArrayList<>();
        for(DictInfo state : states)
        {
            if(state.getDictDesc().endsWith("区"))
            {
                list.add(state);
            }
        }
        return list;
    }

    public static boolean isRobotUser(Long usertype)
    {
        boolean flag = false;
        for(DictInfo dictInfo : DictInfoCache.dictInfos)
        {
            if(dictInfo.getId().equals(usertype) && "ROBOT".equals(dictInfo.getDictValue()))
            {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public static Long getRobotUserType() {
        Long id = null;
        for(DictInfo dictInfo : DictInfoCache.dictInfos)
        {
            if("USER_TYPE".equals(dictInfo.getDictType()) && "ROBOT".equals(dictInfo.getDictValue()))
            {
                id = dictInfo.getId();
                break;
            }
        }
        return id;
    }

    public static boolean isMale(Long gender) {
        boolean m = false;
        for(DictInfo dictInfo : DictInfoCache.dictInfos)
        {
            if(dictInfo.getId().equals(gender) &&  "Male".equals(dictInfo.getDictValue()))
            {
                m = true;
                break;
            }
        }
        return m;
    }

    public static Long getRandomSchoolType() {
        List<DictInfo> schoolType = getByDictType("School");
        Random random = new Random();
        return schoolType.get(random.nextInt(schoolType.size())).getId();
    }
}
