package com.xunli.manager.service;

import com.xunli.manager.model.ChildrenBaseInfo;
import com.xunli.manager.model.ChildrenExtendInfo;
import com.xunli.manager.model.CommonUser;
import com.xunli.manager.model.DictInfo;
import com.xunli.manager.util.ConstantValueUtil;
import com.xunli.manager.util.DictInfoUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by shihj on 2017/7/30.
 */
@Service("generateService")
public class GenerateService {

    private final static int ONE = 3000;
    private final static int TWO = 2000;
    /**
     * 根据数量创建机器用户
     * @param
     * @return
     */
    public  List<CommonUser> generateRobotUser(List<DictInfo> dictInfos)
    {
        List<CommonUser> list = new ArrayList();
        DictInfo userType = DictInfoUtil.getByDictTypeAndDictValue(dictInfos,"USER_TYPE","ROBOT");
        List<DictInfo> gender = DictInfoUtil.getByDictType(dictInfos,"Gender");
        List<DictInfo> home = DictInfoUtil.getByDictType(dictInfos,"Province");
        for(DictInfo province : home)
        {
            DictInfo city = DictInfoUtil.getByDictTypeAndDictValue(dictInfos,"Province",province.getDictValue());
            int number = TWO;
            if(checkIfOne(city))
            {
                number = ONE;
            }
            for(int i = 0; i < number;i++)
            {
                CommonUser user = new CommonUser();
                user.setUsertype(userType);
                user.setLocation(province.getDictDesc() + "," + city.getDictDesc());
                list.add(user);
            }
        }
        return list;
    }

    /**
     * 创建子女信息策略
     * @param user
     * @return
     */
    public List<ChildrenBaseInfo> generateChildrenBaseInfo(List<CommonUser> user,List<DictInfo> dictInfos)
    {
        List<ChildrenBaseInfo> list = new ArrayList<ChildrenBaseInfo>();
        DictInfo male = DictInfoUtil.getByDictTypeAndDictValue(dictInfos,"Gender","Male");
        DictInfo female = DictInfoUtil.getByDictTypeAndDictValue(dictInfos,"Gender","Female");
        for(int i = 0; i < user.size();i++)
        {

            ChildrenBaseInfo children = new ChildrenBaseInfo();
            children.setGender(i % 2 == 0 ? male : female);
            children.setName(createName(children.getGender()));
            children.setEducation(createEducation(user.get(i),DictInfoUtil.getByDictType(dictInfos,"Education")));

            children.setBornLocation(createBornLocation(user.get(i),dictInfos));
            children.setCurrentLocation(createCurrentLocation(children,dictInfos));

            children.setBirthday(createBirthday(children));
            children.setHeight(createHeight(children));
            children.setParentId(user.get(i).getId());
            children.setScore(createChildBaseScore(children));
            children.setLabel("");
            list.add(children);
        }
        return list;
    }

    /**
     * 创建子女扩展信息
     * @param children
     * @return
     */
    public List<ChildrenExtendInfo> generateChildrenExtendInfo(List<ChildrenBaseInfo> children,List<DictInfo> dictInfos)
    {
        List<ChildrenExtendInfo> list = new ArrayList<ChildrenExtendInfo>();
        for(int i = 0; i < children.size();i++)
        {
            ChildrenExtendInfo extend = new ChildrenExtendInfo();
            extend.setCar(createCar());
            extend.setChildrenId(children.get(i).getId());
            extend.setHouse(createHouse(children.get(i),dictInfos));
            extend.setIncome(createIncome(children.get(i),DictInfoUtil.getByDictType(dictInfos,"Income")));
            extend.setCompany(createCompany(children.get(i),DictInfoUtil.getByDictType(dictInfos,"Company")));
            extend.setProfession("");
            extend.setPosition("");
            extend.setSchool("");
            extend.setHobby(createHobby());
            extend.setScore(createChildExtendScore(extend));
            list.add(extend);
        }
        return list;
    }

    /**
     * 生成用户名策略
     * @param gender
     * @return
     */
    private String createName(DictInfo gender)
    {
        return ConstantValueUtil.FIRST_NAME_LIST[(int)(Math.random() * (ConstantValueUtil.FIRST_NAME_LIST.length - 1))].trim() +
                ("Male".equals(gender.getDictValue()) ? "先生" : "小姐");
    }

    /**
     * 检查待生成城市是否为一线城市
     * @param city
     * @return
     */
    private boolean checkIfOne(DictInfo city)
    {
        return city.getDictDesc().contains("北京") ||
                city.getDictDesc().contains("上海") ||
                 city.getDictDesc().contains("深圳") ||
                  city.getDictDesc().contains("广州");
    }

    /**
     * 检查是否为一线城市
     * @param location
     * @return
     */
    private boolean checkIfOne(String location)
    {
        return location.contains("北京") ||
                location.contains("上海") ||
                 location.contains("深圳") ||
                  location.contains("广州");
    }

    /**
     * 检查是否为白领
     * @param edu
     * @return
     */
    private boolean checkEducation(DictInfo edu)
    {
        return edu.getDictDesc().contains("大专") ||
                edu.getDictDesc().contains("本科") ||
                 edu.getDictDesc().contains("硕士") ||
                  edu.getDictDesc().contains("博士");
    }

    /**
     * 生成出生地策略,暂时跟用户所在地保持一致
     * @param
     * @return
     */
    private String createBornLocation(CommonUser user,List<DictInfo> list)
    {
        return user.getLocation();
    }

    /**
     * 生成当前所在地策略
     * @param
     * @return
     */
    private String createCurrentLocation(ChildrenBaseInfo children,List<DictInfo> list)
    {
        //出生地为一线城市
        int v = (int)(Math.random() * 100);
        if(DictInfoUtil.getOneLineCity(list).contains(children.getBornLocation()))
        {
            if(v <= 50)
            {
                return children.getBornLocation();
            }
            else if(v > 50 && v <= 80)
            {
                List<String> other = DictInfoUtil.getOneLineCityExcept(list,children.getBornLocation());
                return other.get((int)(Math.random() * (other.size() - 1)));
            }
            else
            {
                List<String> two = DictInfoUtil.getTwoLineCity(list);
                return two.get((int)(Math.random() * (two.size() - 1)));
            }
        }
        //非一线城市
        if(v <= 70)
        {
            return children.getBornLocation();
        }
        else
        {
            List<String> one = DictInfoUtil.getOneLineCity(list);
            return one.get((int)(Math.random() * (one.size() - 1)));
        }
    }

    /**
     * 生成教育程度策略
     * @param
     * @return
     */
    private DictInfo createEducation(CommonUser user,List<DictInfo> education)
    {
        education.sort(new Comparator<DictInfo>() {
            @Override
            public int compare(DictInfo o1, DictInfo o2) {
                return Integer.parseInt(o1.getDictValue()) - Integer.parseInt(o2.getDictValue());
            }
        });
        if(checkIfOne(user.getLocation()))
            return ((int)(Math.random() * 100)) <= 90 ? education.subList(1,education.size()).get((int)(Math.random() * 4)) : education.get(0);
        return ((int)(Math.random() * 100)) <= 70 ? education.subList(1,education.size()).get((int)(Math.random() * 4)) : education.get(0);
    }

    /**
     * 生成出生年份策略
     * @param
     * @return
     */
    private String createBirthday(ChildrenBaseInfo children)
    {
        int v = (int)(Math.random() * 100);
        if(v <= 10)
        {
            List<String> birth = Arrays.asList(Arrays.copyOfRange(ConstantValueUtil.BIRTHDAY,0,10));
            return birth.get((int)(Math.random()*(birth.size() - 1)));

        }
        else if(v > 10 && v <= 30)
        {
            List<String> birth = Arrays.asList(Arrays.copyOfRange(ConstantValueUtil.BIRTHDAY,11,15));
            return birth.get((int)(Math.random()*(birth.size() - 1)));
        }
        else if(v > 30 && v < 80)
        {
            List<String> birth = Arrays.asList(Arrays.copyOfRange(ConstantValueUtil.BIRTHDAY,16,22));
            return birth.get((int)(Math.random()*(birth.size() - 1)));
        }
        else
        {
            List<String> birth = Arrays.asList(Arrays.copyOfRange(ConstantValueUtil.BIRTHDAY,23,ConstantValueUtil.BIRTHDAY.length));
            return birth.get((int)(Math.random()*(birth.size() - 1)));
        }
    }

    /**
     * 生成升高策略
     * @param
     * @return
     */
    private Integer createHeight(ChildrenBaseInfo children)
    {
        int v = (int)(Math.random() * 100);
        if("Male".equals(children.getGender().getDictValue()))
        {
            if(v <= 5)
            {
                Integer[] height = Arrays.copyOfRange(ConstantValueUtil.HEIGHT,5,25);

                return height[(int)(Math.random() * (height.length - 1))];
            }
            else if(5 < v && v <= 55)
            {
                Integer[] height = Arrays.copyOfRange(ConstantValueUtil.HEIGHT,25,30);
                return height[(int)(Math.random() * (height.length - 1))];
            }
            else if(55 < v && v <= 75)
            {
                Integer[] height = Arrays.copyOfRange(ConstantValueUtil.HEIGHT,30,35);
                return height[(int)(Math.random() * (height.length - 1))];
            }
            else if(75 < v && v <= 95)
            {
                Integer[] height = Arrays.copyOfRange(ConstantValueUtil.HEIGHT,35,45);
                return height[(int)(Math.random() * (height.length - 1))];
            }
            else
            {
                Integer[] height = Arrays.copyOfRange(ConstantValueUtil.HEIGHT,45,ConstantValueUtil.HEIGHT.length);
                return height[(int)(Math.random() * (height.length - 1))];
            }
        }
        else
        {
            if(v <= 5)
            {
                Integer[] height = Arrays.copyOfRange(ConstantValueUtil.HEIGHT,0,10);

                return height[(int)(Math.random() * (height.length - 1))];
            }
            else if(5 < v && v <= 55)
            {
                Integer[] height = Arrays.copyOfRange(ConstantValueUtil.HEIGHT,10,15);
                return height[(int)(Math.random() * (height.length - 1))];
            }
            else if(55 < v && v <= 75)
            {
                Integer[] height = Arrays.copyOfRange(ConstantValueUtil.HEIGHT,15,20);
                return height[(int)(Math.random() * (height.length - 1))];
            }
            else if(75 < v && v <= 95)
            {
                Integer[] height = Arrays.copyOfRange(ConstantValueUtil.HEIGHT,20,25);
                return height[(int)(Math.random() * (height.length - 1))];
            }
            else
            {
                Integer[] height = Arrays.copyOfRange(ConstantValueUtil.HEIGHT,25,ConstantValueUtil.HEIGHT.length);
                return height[(int)(Math.random() * (height.length - 1))];
            }
        }
    }


    /**
     * 创建基础信息评分
     * @param child
     * @return
     */
    private Integer createChildBaseScore(ChildrenBaseInfo child)
    {
        int score = 0;
        if(child != null)
        {
            if("Male".equals(child.getGender().getDictValue()))
            {
                if(child.getHeight() < 170)
                    score += 1;
                else if(child.getHeight() >= 170 && child.getHeight() <= 175)
                    score += 2;
                else if(child.getHeight() >= 176 && child.getHeight() <= 190)
                    score += 3;
                else
                    score += 2;
            }
            else
            {
                if(child.getHeight() < 155)
                    score += 1;
                else if(child.getHeight() >= 155 && child.getHeight() <= 160)
                    score += 2;
                else if(child.getHeight() >= 161 && child.getHeight() <= 165)
                    score += 2;
                else if(child.getHeight() >= 166 && child.getHeight() <= 170)
                    score += 3;
                else
                    score += 3;
            }

        }
        return score;
    }

    /**
     * 创建扩展信息评分
     * @param extend
     * @return
     */
    private Integer createChildExtendScore(ChildrenExtendInfo extend)
    {
        int score = 0;
        if(extend != null)
        {
            //是否有车评分
            if(extend.getCar())
            {
                score += 2;
            }
            else
            {
                score += 1;
            }
            //是否有房评分
            if("1".equals(extend.getHouse().getDictValue()))
            {
                score += 1;
            }
            else if("2".equals(extend.getHouse().getDictValue()))
            {
                score += 3;
            }
            else if("3".equals(extend.getHouse().getDictValue()))
            {
                score += 5;
            }
            //工作单位评分
            if(extend.getCompany().contains("政府"))
            {
                score += 3;
            }
            else if(extend.getCompany().contains("事业单位"))
            {
                score += 2;
            }
            else if(extend.getCompany().contains("国企"))
            {
                score += 2;
            }
            else if(extend.getCompany().contains("外企"))
            {
                score += 3;
            }
            else if(extend.getCompany().contains("民企"))
            {
                score += 1;
            }
            //收入评分
            if("1".equals(extend.getIncome().getDictValue()))
            {
                score += 1;
            }
            else if("2".equals(extend.getIncome().getDictValue()))
            {
                score += 2;
            }
            else if("3".equals(extend.getIncome().getDictValue()))
            {
                score += 3;
            }
            else if("4".equals(extend.getIncome().getDictValue()))
            {
                score += 4;
            }
            else if("5".equals(extend.getIncome().getDictValue()))
            {
                score += 5;
            }
            //爱好评分
            String[] hobby = extend.getHobby().split(",");
            if(hobby.length <= 2)
            {
                score += 1;
            }
            else if(hobby.length >2 && hobby.length <=4)
            {
                score += 2;
            }
            else
            {
                score += 3;
            }
        }
        return score;
    }

    /**
     * 生成房产策略
     * @param children
     * @param list
     * @return
     */
    private DictInfo createHouse(ChildrenBaseInfo children,List<DictInfo> list)
    {
        int v = (int) (Math.random() * 100);
        if("Male".equals(children.getGender().getDictValue()))
        {
            if(v < 30)
            {
                return DictInfoUtil.getByDictTypeAndDictValue(list,"House","1");
            }
            else if(30 <= v && v < 90)
            {
                return DictInfoUtil.getByDictTypeAndDictValue(list,"House","2");
            }
            else
            {
                return DictInfoUtil.getByDictTypeAndDictValue(list,"House","3");
            }
        }
        else
        {
            if(v < 70)
            {
                return DictInfoUtil.getByDictTypeAndDictValue(list,"House","1");
            }
            else if(70 <= v && v < 95)
            {
                return DictInfoUtil.getByDictTypeAndDictValue(list,"House","2");
            }
            else
            {
                return DictInfoUtil.getByDictTypeAndDictValue(list,"House","3");
            }
        }
    }


    /**
     * 是否有车生成策略
     * @return
     */
    private boolean createCar()
    {
        return (int)(Math.random() * 100) < 30 ? false : true;
    }

    /**
     * 收入生成策略
     * @param children
     * @param income
     * @return
     */
    private DictInfo createIncome(ChildrenBaseInfo children,List<DictInfo> income)
    {
        return income.get((int)(Math.random() * (income.size() - 1)));
    }

    /**
     * 生成爱好策略
     * @return
     */
    private String createHobby()
    {
        int v = (int)(Math.random() * 100);
        if(v < 40)
        {
            return arrayToString(rangeGet(ConstantValueUtil.HOBBY,2));
        }
        else if(40 <= v && v < 80)
        {
            return arrayToString(rangeGet(ConstantValueUtil.HOBBY,3));
        }
        else
        {
            return arrayToString(rangeGet(ConstantValueUtil.HOBBY,5));
        }
    }

    private String[] rangeGet(String[] src,int num)
    {
        if(src.length <= num)
            return src;
        String[] res = new String[num];
        for(int i = 0 ; i < num ; i ++)
        {
            while(true)
            {
                int pos = (int)(Math.random() * (src.length - 1));
                if(!"".equals(src[pos]))
                {
                    res[i] = src[pos];
                    src[pos] = "";
                    break;
                }
            }
        }
        return res;
    }

    /**
     * 生成公司类型信息
     * 白领:政府（20%）、事业单位（20%）、国企（20%）、外企（10%）、民企（30%）
     * 蓝领:政府（5%）、事业单位（10%）、国企（20%）、外企（0%）、民企（65%）
     * @param children
     * @param company
     * @return
     */
    private String createCompany(ChildrenBaseInfo children,List<DictInfo> company)
    {
        int v = (int)(Math.random() * 100);
        if(checkEducation(children.getEducation()))
        {
            if(v < 20)
            {
                return DictInfoUtil.getByDictTypeAndDictValue(company,"Company","1").getDictDesc();
            }
            else if(20 <= v && v < 40)
            {
                return DictInfoUtil.getByDictTypeAndDictValue(company,"Company","2").getDictDesc();
            }
            else if(40 <= v && v < 60)
            {
                return DictInfoUtil.getByDictTypeAndDictValue(company,"Company","3").getDictDesc();
            }
            else if(60 <= v && v < 70)
            {
                return DictInfoUtil.getByDictTypeAndDictValue(company,"Company","4").getDictDesc();
            }
            else
            {
                return DictInfoUtil.getByDictTypeAndDictValue(company,"Company","5").getDictDesc();
            }

        }
        if(v < 5)
        {
            return DictInfoUtil.getByDictTypeAndDictValue(company,"Company","1").getDictDesc();
        }
        else if(5 <= v && v < 15)
        {
            return DictInfoUtil.getByDictTypeAndDictValue(company,"Company","2").getDictDesc();
        }
        else if(15 <= v && v < 35)
        {
            return DictInfoUtil.getByDictTypeAndDictValue(company,"Company","3").getDictDesc();
        }
        else
        {
            return DictInfoUtil.getByDictTypeAndDictValue(company,"Company","5").getDictDesc();
        }
    }

    private String arrayToString(String[] arr)
    {
        StringBuffer sb = new StringBuffer();
        if(arr.length == 0)
            return "";
        for(String v : arr)
            sb.append(v).append(",");
        return sb.substring(0,sb.lastIndexOf(",")).toString();
    }
}
