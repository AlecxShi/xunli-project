package com.xunli.manager.service;

import com.xunli.manager.model.*;
import com.xunli.manager.util.ConstantValueUtil;
import com.xunli.manager.util.DictInfoUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by shihj on 2017/7/30.
 */
@Service("generateService")
public class GenerateService {

    private final static Integer YEAR = Calendar.getInstance().get(Calendar.YEAR);

    private final static int ONE = 300;
    private final static int TWO = 200;

    public final static int getRandom(int base)
    {
        return (int)(Math.random() * base);
    }
    /**
     * 根据数量创建机器用户
     * @param
     * @return
     */
    public  List<CommonUser> generateRobotUser()
    {
        List<CommonUser> list = new ArrayList();
        DictInfo userType = DictInfoUtil.getByDictTypeAndDictValue("USER_TYPE","ROBOT");
        List<DictInfo> home = DictInfoUtil.getByDictType("Province");
        for(DictInfo province : home)
        {
            List<DictInfo> citys = DictInfoUtil.getByDictType(province.getDictValue());
            int number = TWO;
            for(DictInfo city : citys)
            {
                List<DictInfo> states = DictInfoUtil.getByDictType(city.getDictValue());
                if(checkIfOne(city))
                {
                    number = ONE;
                }
                for(int i = 0; i < number;i++)
                {
                    CommonUser user = new CommonUser();
                    user.setUsertype(userType.getId());
                    user.setLocation(province.getDictDesc() + "," + city.getDictDesc());
                    list.add(user);
                }
            }

        }
        return list;
    }

    /**
     * 创建统一的子女信息
     * @param user
     * @return
     */
    public List<ChildrenInfo> generateChildrenInfo(List<CommonUser> user)
    {
        List<ChildrenInfo> list = new ArrayList<ChildrenInfo>();
        DictInfo male = DictInfoUtil.getByDictTypeAndDictValue("Gender","Male");
        DictInfo female = DictInfoUtil.getByDictTypeAndDictValue("Gender","Female");
        for(int i = 0; i < user.size();i++)
        {

            ChildrenInfo children = new ChildrenInfo();
            children.setParentId(user.get(i).getId());
            children.setGender(i % 2 == 0 ? male.getId() : female.getId());
            children.setName(createName(DictInfoUtil.getItemById(children.getGender())));
            children.setEducation(createEducation(user.get(i),DictInfoUtil.getByDictType("Education")).getId());

            children.setBornLocation(createBornLocation(user.get(i)));
            children.setCurrentLocation(createCurrentLocation(children));

            children.setBirthday(createBirthday(children));
            children.setHeight(createHeight(children));


            children.setCar(createCar());
            children.setHouse(createHouse(children).getId());
            children.setIncome(createIncome(children,DictInfoUtil.getByDictType("Income")).getId());
            children.setCompany(createCompany(children));
            children.setProfession("");
            children.setPosition("");
            children.setSchool("");
            children.setHobby(createHobby());

            children.setScore(createScore(children));
            children.setLabel(createLabel(children));
            list.add(children);
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
        return ConstantValueUtil.FIRST_NAME_LIST[getRandom(ConstantValueUtil.FIRST_NAME_LIST.length - 1)].trim() +
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
        if(edu != null)
            return edu.getDictDesc().contains("大专") ||
                edu.getDictDesc().contains("本科") ||
                 edu.getDictDesc().contains("硕士") ||
                  edu.getDictDesc().contains("博士");
        return false;
    }

    /**
     * 生成出生地策略,暂时跟用户所在地保持一致
     * @param
     * @return
     */
    private String createBornLocation(CommonUser user)
    {
        return user.getLocation();
    }

    /**
     * 生成当前所在地策略
     * @param
     * @return
     */
    private String createCurrentLocation(ChildrenInfo children)
    {
        //出生地为一线城市
        int v = getRandom(100);
        if(DictInfoUtil.getOneLineCity().contains(children.getBornLocation()))
        {
            if(v <= 50)
            {
                return children.getBornLocation();
            }
            else if(v > 50 && v <= 80)
            {
                List<String> other = DictInfoUtil.getOneLineCityExcept(children.getBornLocation());
                return other.get(getRandom(other.size() - 1));
            }
            else
            {
                List<String> two = DictInfoUtil.getTwoLineCity();
                return two.get(getRandom(two.size() - 1));
            }
        }
        //非一线城市
        if(v <= 70)
        {
            return children.getBornLocation();
        }
        else
        {
            List<String> one = DictInfoUtil.getOneLineCity();
            return one.get(getRandom(one.size() - 1));
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
        int v = getRandom(100);
        if(checkIfOne(user.getLocation()))
            return v <= 90 ? education.subList(1,education.size()).get(getRandom(4)) : education.get(0);
        return v <= 70 ? education.subList(1,education.size()).get(getRandom(4)) : education.get(0);
    }

    /**
     * 生成出生年份策略
     * @param
     * @return
     */
    private String createBirthday(ChildrenInfo children)
    {
        int v = getRandom(100);
        int month = getRandom(ConstantValueUtil.MONTH_AND_DAY.length - 1);
        int day = getRandom(ConstantValueUtil.MONTH_AND_DAY[month].length - 1);
        String month_and_day = "-" + (month + 1 < 10 ? "0" + (month + 1) : (month + 1)) + "-" + ConstantValueUtil.MONTH_AND_DAY[month][day];
        if(v <= 10)
        {
            List<String> birth = Arrays.asList(Arrays.copyOfRange(ConstantValueUtil.BIRTHDAY,0,10));
            return birth.get(getRandom(birth.size() - 1)) + month_and_day;

        }
        else if(v > 10 && v <= 30)
        {
            List<String> birth = Arrays.asList(Arrays.copyOfRange(ConstantValueUtil.BIRTHDAY,11,15));
            return birth.get(getRandom(birth.size() - 1)) + month_and_day;
        }
        else if(v > 30 && v < 80)
        {
            List<String> birth = Arrays.asList(Arrays.copyOfRange(ConstantValueUtil.BIRTHDAY,16,22));
            return birth.get(getRandom(birth.size() - 1)) + month_and_day;
        }
        else
        {
            List<String> birth = Arrays.asList(Arrays.copyOfRange(ConstantValueUtil.BIRTHDAY,23,ConstantValueUtil.BIRTHDAY.length));
            return birth.get(getRandom(birth.size() - 1)) + month_and_day;
        }
    }

    /**
     * 生成升高策略
     * @param
     * @return
     */
    private Integer createHeight(ChildrenInfo children)
    {
        int v = getRandom(100);
        if("Male".equals(DictInfoUtil.getItemById(children.getGender()).getDictValue()))
        {
            if(v <= 5)
            {
                Integer[] height = Arrays.copyOfRange(ConstantValueUtil.HEIGHT,5,25);

                return height[getRandom(height.length - 1)];
            }
            else if(5 < v && v <= 55)
            {
                Integer[] height = Arrays.copyOfRange(ConstantValueUtil.HEIGHT,25,30);
                return height[getRandom(height.length - 1)];
            }
            else if(55 < v && v <= 75)
            {
                Integer[] height = Arrays.copyOfRange(ConstantValueUtil.HEIGHT,30,35);
                return height[getRandom(height.length - 1)];
            }
            else if(75 < v && v <= 95)
            {
                Integer[] height = Arrays.copyOfRange(ConstantValueUtil.HEIGHT,35,45);
                return height[getRandom(height.length - 1)];
            }
            else
            {
                Integer[] height = Arrays.copyOfRange(ConstantValueUtil.HEIGHT,45,ConstantValueUtil.HEIGHT.length);
                return height[getRandom(height.length - 1)];
            }
        }
        else
        {
            if(v <= 5)
            {
                Integer[] height = Arrays.copyOfRange(ConstantValueUtil.HEIGHT,0,10);

                return height[getRandom(height.length - 1)];
            }
            else if(5 < v && v <= 55)
            {
                Integer[] height = Arrays.copyOfRange(ConstantValueUtil.HEIGHT,10,15);
                return height[getRandom(height.length - 1)];
            }
            else if(55 < v && v <= 75)
            {
                Integer[] height = Arrays.copyOfRange(ConstantValueUtil.HEIGHT,15,20);
                return height[getRandom(height.length - 1)];
            }
            else if(75 < v && v <= 95)
            {
                Integer[] height = Arrays.copyOfRange(ConstantValueUtil.HEIGHT,20,25);
                return height[getRandom(height.length - 1)];
            }
            else
            {
                Integer[] height = Arrays.copyOfRange(ConstantValueUtil.HEIGHT,25,ConstantValueUtil.HEIGHT.length);
                return height[getRandom(height.length - 1)];
            }
        }
    }


    /**
     * 创建基础信息评分
     * @param child
     * @return
     */
    public static Integer createScore(ChildrenInfo child)
    {
        int score = 0;
        if(child != null)
        {
            if("Male".equals(DictInfoUtil.getItemById(child.getGender()).getDictValue()))
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
            //是否有车评分
            if(child.getCar())
            {
                score += 2;
            }
            else
            {
                score += 1;
            }
            //是否有房评分
            if("1".equals(DictInfoUtil.getItemById(child.getHouse()).getDictValue()))
            {
                score += 1;
            }
            else if("2".equals(DictInfoUtil.getItemById(child.getHouse()).getDictValue()))
            {
                score += 3;
            }
            else if("3".equals(DictInfoUtil.getItemById(child.getHouse()).getDictValue()))
            {
                score += 5;
            }
            //工作单位评分
            if(DictInfoUtil.getItemById(child.getCompany()).getDictDesc().contains("政府"))
            {
                score += 3;
            }
            else if(DictInfoUtil.getItemById(child.getCompany()).getDictDesc().contains("事业单位"))
            {
                score += 2;
            }
            else if(DictInfoUtil.getItemById(child.getCompany()).getDictDesc().contains("国企"))
            {
                score += 2;
            }
            else if(DictInfoUtil.getItemById(child.getCompany()).getDictDesc().contains("外企"))
            {
                score += 3;
            }
            else
            {
                score += 1;
            }
            //收入评分
            if("1".equals(DictInfoUtil.getItemById(child.getIncome()).getDictValue()))
            {
                score += 1;
            }
            else if("2".equals(DictInfoUtil.getItemById(child.getIncome()).getDictValue()))
            {
                score += 2;
            }
            else if("3".equals(DictInfoUtil.getItemById(child.getIncome()).getDictValue()))
            {
                score += 3;
            }
            else if("4".equals(DictInfoUtil.getItemById(child.getIncome()).getDictValue()))
            {
                score += 4;
            }
            else if("5".equals(DictInfoUtil.getItemById(child.getIncome()).getDictValue()))
            {
                score += 5;
            }
            //爱好评分
            String[] hobby = child.getHobby().split(",");
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
     * 创建标签策略
     * @param child
     * @return
     */
    public  static String createLabel(ChildrenInfo child) {
        StringBuffer sb = new StringBuffer();
        if(child != null)
        {
            if(DictInfoUtil.getItemById(child.getCompany()).getDictDesc().contains("政府"))
            {
                sb.append("公务员").append(",");
            }
            else if(DictInfoUtil.getItemById(child.getCompany()).getDictDesc().contains("外企"))
            {
                sb.append("海外名企").append(",");
            }
            if("4".equals(DictInfoUtil.getItemById(child.getIncome()).getDictValue()) || "5".equals(DictInfoUtil.getItemById(child.getIncome()).getDictValue()))
            {
                sb.append("多金优质").append(",");
            }
            if("3".equals(DictInfoUtil.getItemById(child.getHouse()).getDictValue()))
            {
                sb.append("坐拥多套房产").append(",");
            }
            if(child.getHobby().split(",").length >= 5)
            {
                sb.append("多才多艺").append(",");
            }
        }
        return sb.subSequence(0,sb.lastIndexOf(",") < 0 ? 0 : sb.lastIndexOf(",")).toString();
    }


    /**
     * 生成房产策略
     * @param children
     * @return
     */
    private DictInfo createHouse(ChildrenInfo children)
    {
        int v = getRandom(100);
        if("Male".equals(DictInfoUtil.getItemById(children.getGender()).getDictValue()))
        {
            if(v < 30)
            {
                return DictInfoUtil.getByDictTypeAndDictValue("House","1");
            }
            else if(30 <= v && v < 90)
            {
                return DictInfoUtil.getByDictTypeAndDictValue("House","2");
            }
            else
            {
                return DictInfoUtil.getByDictTypeAndDictValue("House","3");
            }
        }
        else
        {
            if(v < 70)
            {
                return DictInfoUtil.getByDictTypeAndDictValue("House","1");
            }
            else if(70 <= v && v < 95)
            {
                return DictInfoUtil.getByDictTypeAndDictValue("House","2");
            }
            else
            {
                return DictInfoUtil.getByDictTypeAndDictValue("House","3");
            }
        }
    }


    /**
     * 是否有车生成策略
     * @return
     */
    private boolean createCar()
    {
        return getRandom(100) < 30 ? false : true;
    }

    /**
     * 收入生成策略
     * @param children
     * @param income
     * @return
     */
    private DictInfo createIncome(ChildrenInfo children,List<DictInfo> income)
    {
        return income.get(getRandom(income.size() - 1));
    }

    /**
     * 生成爱好策略
     * @return
     */
    private String createHobby()
    {
        int v = getRandom(100);
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
        //return src;
        String[] des = Arrays.copyOfRange(src,0,src.length);
        if(des.length <= num)
            return src;
        String[] res = new String[num];
        for(int i = 0 ; i < num ; i ++)
        {
            while(true)
            {
                int pos = (int)(Math.random() * (des.length - 1));
                if(!"".equals(des[pos]))
                {
                    res[i] = des[pos];
                    des[pos] = "";
                    break;
                }
            }
        }
        System.out.println(Arrays.toString(res));
        return res;
    }

    /*public static  void main(String[] args)
    {
        GenerateService gs = new GenerateService();
        for(int i = 0; i < 10;i++)
            gs.rangeGet(ConstantValueUtil.HOBBY,2);
    }*/

    /**
     * 生成公司类型信息
     * 白领:政府（20%）、事业单位（20%）、国企（20%）、外企（10%）、民企（30%）
     * 蓝领:政府（5%）、事业单位（10%）、国企（20%）、外企（0%）、民企（65%）
     * @param children
     * @return
     */
    private Long createCompany(ChildrenInfo children)
    {
        int v = getRandom(100);
        if(checkEducation(DictInfoUtil.getItemById(children.getEducation())))
        {
            if(v < 20)
            {
                return DictInfoUtil.getByDictTypeAndDictValue("Company","1").getId();
            }
            else if(20 <= v && v < 40)
            {
                return DictInfoUtil.getByDictTypeAndDictValue("Company","2").getId();
            }
            else if(40 <= v && v < 60)
            {
                return DictInfoUtil.getByDictTypeAndDictValue("Company","3").getId();
            }
            else if(60 <= v && v < 70)
            {
                return DictInfoUtil.getByDictTypeAndDictValue("Company","4").getId();
            }
            else
            {
                return DictInfoUtil.getByDictTypeAndDictValue("Company","5").getId();
            }

        }
        if(v < 5)
        {
            return DictInfoUtil.getByDictTypeAndDictValue("Company","1").getId();
        }
        else if(5 <= v && v < 15)
        {
            return DictInfoUtil.getByDictTypeAndDictValue("Company","2").getId();
        }
        else if(15 <= v && v < 35)
        {
            return DictInfoUtil.getByDictTypeAndDictValue("Company","3").getId();
        }
        else
        {
            return DictInfoUtil.getByDictTypeAndDictValue("Company","5").getId();
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
