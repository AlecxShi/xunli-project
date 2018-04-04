package com.xunli.manager.service;

import com.xunli.manager.cache.ColleageCache;
import com.xunli.manager.common.Const;
import com.xunli.manager.domain.criteria.RobotUserCheckConditionCriteria;
import com.xunli.manager.enumeration.ReturnCode;
import com.xunli.manager.job.UpdateRobotUserIconJob;
import com.xunli.manager.model.*;
import com.xunli.manager.repository.*;
import com.xunli.manager.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Betty on 2017/7/16.
 */
@Service("commonUserService")
public class CommonUserService {

    @Autowired
    private CommonUserRepository commonUserRepository;

    @Autowired
    private ChildrenInfoTwoRepository childrenInfoTwoRepository;

    @Autowired
    private ChildrenInfoRepository childrenInfoRepository;

    @Autowired
    private RecommendInfoTwoRepository recommendInfoTwoRepository;

    @Autowired
    private CommonUserLoginsRepository commonUserLoginsRepository;

    @Autowired
    private TaobaoIMService taobaoIMService;

    private final static Random rand = new Random();

    @Transactional
    public RequestResult login(CommonUser user, HttpServletRequest request)
    {
        CommonUserLogins login = commonUserLoginsRepository.findOneByUserId(user.getId());
        Boolean ifFirstLogin = false;
        if(login == null)
        {
            login = new CommonUserLogins();
            login.setUserId(user.getId());
            login.setToken(RandomUtil.generateToken());
            login.setIpAddress(request.getRemoteAddr());
            login.setUserAgent(request.getHeader("User-Agent"));
            login.setLastUsed(new Date());
            Calendar instance = Calendar.getInstance();
            instance.add(Calendar.MONTH,1);
            login.setExpireTime(instance.getTime());
            commonUserLoginsRepository.save(login);
        }
        else if(login.getExpireTime().compareTo(new Date()) <= 0)
        {
            login.setToken(RandomUtil.generateToken());
            login.setIpAddress(request.getRemoteAddr());
            login.setUserAgent(request.getHeader("User-Agent"));
            login.setLastUsed(new Date());
            Calendar instance = Calendar.getInstance();
            instance.add(Calendar.MONTH,1);
            login.setExpireTime(instance.getTime());
            commonUserLoginsRepository.save(login);
        }
        else
        {
            login.setIpAddress(request.getRemoteAddr());
            login.setUserAgent(request.getHeader("User-Agent"));
            login.setLastUsed(new Date());
            commonUserLoginsRepository.save(login);
        }

        ChildrenInfo childrenInfo = childrenInfoRepository.findOneByParentId(login.getUserId());
        //没有基本信息ifFirstLogin = true
        //有的话则为false
        if(childrenInfo == null || childrenInfo.getName() == null || childrenInfo.getBirthday() == null || childrenInfo.getEducation() == null)
        {
            ifFirstLogin = true;
        }
        Map<String,Object> result = new HashMap();
        result.put("token",login.getToken());
        result.put("ifFirstLogin",ifFirstLogin);
        result.put("userId", MD5Util.Encode(String.valueOf(user.getId())));
        result.put("password",user.getPassword());
        return new RequestResult(ReturnCode.PUBLIC_SUCCESS,result);
    }


    /**
     * 生成10条假数据
     * @param conditionCriteria
     * @return
     */
    public List<CommonUser> generateTenRobotUsers(RobotUserCheckConditionCriteria conditionCriteria)
    {
        List<CommonUser> users = new ArrayList<>();
        int num = 10;
        String bornLocation = conditionCriteria.getBornLocation();
        String currentLocation = conditionCriteria.getCurrentLocation();
        Long gender = conditionCriteria.getGender();
        String birthday = conditionCriteria.getBirthday();
        if(!StringUtils.isEmpty(bornLocation) && !StringUtils.isEmpty(currentLocation) && gender != null)
        {
            //当前用户是女的
            if(Const.MALE.equals(DictInfoUtil.getOppositeSex(DictInfoUtil.getItemById(gender)).getDictValue()))
            {
                for(int i = 0; i < num;i++)
                {
                    CommonUser u = new CommonUser();
                    u.setIfRegister("N");
                    u.setUsertype(80L);
                    u.setLocation(bornLocation);
                    String username = ConstantValueUtil.FIRST_NAME_LIST[rand.nextInt(ConstantValueUtil.FIRST_NAME_LIST.length)] + "先生";
                    u.setUsername(username);
                    CommonUtil.encrypPassword(u);
                    ChildrenInfo childrenInfo = new ChildrenInfo();
                    childrenInfo.setBornLocation(bornLocation);
                    childrenInfo.setCurrentLocation(currentLocation);
                    childrenInfo.setName(username);
                    childrenInfo.setGender(68L);
                    childrenInfo.setIcon(UpdateRobotUserIconJob.getIconPath(childrenInfo));
                    if(i % 2 == 0){
                        childrenInfo.setCar(162L);
                    }else{
                        childrenInfo.setCar(163L);
                    }
                    childrenInfo.setSchoolType(DictInfoUtil.getRandomSchoolType());
                    childrenInfo.setSchool(ColleageCache.commonCollege.get(rand.nextInt(ColleageCache.commonCollege.size())));
                    if(i % 3 == 0 && i != 0)
                        childrenInfo.setHouse(76L);
                    else if(i % 4 == 0 && i != 0)
                        childrenInfo.setHouse(78L);
                    else
                        childrenInfo.setHouse(77L);
                    childrenInfo.setHobby(createHobby(rand.nextInt(ConstantValueUtil.HOBBY.length)));
                    childrenInfo.setBirthday(createBirthday(birthday,-10,10));
                    u.setChildren(childrenInfo);
                    users.add(u);
                }
                createHeight(users);
                createEducation(users);
                createIncome(users);

            }
            else
            {
                for(int i = 0; i < num;i++)
                {
                    CommonUser u = new CommonUser();
                    u.setIfRegister("N");
                    u.setUsertype(80L);
                    u.setLocation(bornLocation);
                    String username = ConstantValueUtil.FIRST_NAME_LIST[rand.nextInt(ConstantValueUtil.FIRST_NAME_LIST.length)] + "女士";
                    u.setUsername(username);
                    CommonUtil.encrypPassword(u);
                    ChildrenInfo childrenInfo = new ChildrenInfo();
                    childrenInfo.setBornLocation(bornLocation);
                    childrenInfo.setCurrentLocation(currentLocation);
                    childrenInfo.setName(username);
                    childrenInfo.setGender(69L);
                    String icon = UpdateRobotUserIconJob.getIconPath(childrenInfo);
                    childrenInfo.setIcon(icon);
                    if(i % 2 == 0)
                    {
                        childrenInfo.setCar(162L);
                    }
                    else
                    {
                        childrenInfo.setCar(163L);
                    }
                    childrenInfo.setSchoolType(DictInfoUtil.getRandomSchoolType());
                    childrenInfo.setSchool(ColleageCache.commonCollege.get(rand.nextInt(ColleageCache.commonCollege.size())));
                    if(i % 3 == 0 && i != 0)
                        childrenInfo.setHouse(76L);
                    else if(i % 4 == 0 && i != 0)
                        childrenInfo.setHouse(78L);
                    else
                        childrenInfo.setHouse(77L);
                    childrenInfo.setHobby(createHobby(rand.nextInt(ConstantValueUtil.HOBBY.length)));
                    childrenInfo.setBirthday(createBirthday(birthday,-10,10));
                    u.setChildren(childrenInfo);
                    users.add(u);
                }
                createHeight(users);
                createEducation(users);
                createIncome(users);
            }
        }
        return users;
    }

    /**
     * 创建60条数据
     * @param conditionCriteria
     * @return
     */
    public List<CommonUser> generateOtherRobotUsers(RobotUserCheckConditionCriteria conditionCriteria,int count)
    {
        List<CommonUser> users = new ArrayList<>();
        //取10的倍数
        count = count % 10 == 0 ? count : (count / 10 + 1) * 10;
        String bornLocation = conditionCriteria.getBornLocation();
        String currentLocation = conditionCriteria.getCurrentLocation();
        Long gender = conditionCriteria.getGender();
        String birthday = conditionCriteria.getBirthday();
        if(!StringUtils.isEmpty(bornLocation) && !StringUtils.isEmpty(currentLocation) && gender != null)
        {
            //当前用户是女的
            if(Const.MALE.equals(DictInfoUtil.getOppositeSex(DictInfoUtil.getItemById(gender)).getDictValue()))
            {
                for(int i = 0; i < count;i++)
                {
                    CommonUser u = new CommonUser();
                    u.setIfRegister("N");
                    u.setUsertype(80L);
                    u.setLocation(bornLocation);
                    String username = ConstantValueUtil.FIRST_NAME_LIST[rand.nextInt(ConstantValueUtil.FIRST_NAME_LIST.length)] + "先生";
                    u.setUsername(username);
                    CommonUtil.encrypPassword(u);
                    ChildrenInfo childrenInfo = new ChildrenInfo();
                    childrenInfo.setBornLocation(bornLocation);
                    childrenInfo.setCurrentLocation(currentLocation);
                    childrenInfo.setName(username);
                    childrenInfo.setGender(68L);
                    childrenInfo.setIcon(UpdateRobotUserIconJob.getIconPath(childrenInfo));
                    if(i % 2 == 0){
                        childrenInfo.setCar(162L);
                    }else{
                        childrenInfo.setCar(163L);
                    }
                    childrenInfo.setSchoolType(DictInfoUtil.getRandomSchoolType());
                    childrenInfo.setSchool(ColleageCache.commonCollege.get(rand.nextInt(ColleageCache.commonCollege.size())));
                    if(i % 3 == 0 && i != 0)
                        childrenInfo.setHouse(76L);
                    else if(i % 4 == 0 && i != 0)
                        childrenInfo.setHouse(78L);
                    else
                        childrenInfo.setHouse(77L);
                    childrenInfo.setHobby(createHobby(rand.nextInt(ConstantValueUtil.HOBBY.length)));
                    if(i < count * 0.5){
                        childrenInfo.setBirthday(createBirthday(birthday,-10,0));
                    }else if(i >= count * 0.5 && i < count * 0.8){
                        childrenInfo.setBirthday(createBirthday(birthday,0,0));
                    }else{
                        childrenInfo.setBirthday(createBirthday(birthday,0,3));
                    }
                    u.setChildren(childrenInfo);
                    users.add(u);
                }
                createHeight(users);
                createEducation(users);
                createIncome(users);

            }
            else
            {
                for(int i = 0; i < count;i++)
                {
                    CommonUser u = new CommonUser();
                    u.setIfRegister("N");
                    u.setUsertype(80L);
                    u.setLocation(bornLocation);
                    String username = ConstantValueUtil.FIRST_NAME_LIST[rand.nextInt(ConstantValueUtil.FIRST_NAME_LIST.length)] + "女士";
                    u.setUsername(username);
                    CommonUtil.encrypPassword(u);
                    ChildrenInfo childrenInfo = new ChildrenInfo();
                    childrenInfo.setBornLocation(bornLocation);
                    childrenInfo.setCurrentLocation(currentLocation);
                    childrenInfo.setName(username);
                    childrenInfo.setGender(69L);
                    String icon = UpdateRobotUserIconJob.getIconPath(childrenInfo);
                    childrenInfo.setIcon(icon);
                    if(i % 2 == 0)
                    {
                        childrenInfo.setCar(162L);
                    }
                    else
                    {
                        childrenInfo.setCar(163L);
                    }
                    childrenInfo.setSchoolType(DictInfoUtil.getRandomSchoolType());
                    childrenInfo.setSchool(ColleageCache.commonCollege.get(rand.nextInt(ColleageCache.commonCollege.size())));
                    if(i % 3 == 0 && i != 0)
                        childrenInfo.setHouse(76L);
                    else if(i % 4 == 0 && i != 0)
                        childrenInfo.setHouse(78L);
                    else
                        childrenInfo.setHouse(77L);
                    childrenInfo.setHobby(createHobby(rand.nextInt(ConstantValueUtil.HOBBY.length)));
                    //生成出生日期
                    if(i < count * 0.2){
                        childrenInfo.setBirthday(createBirthday(birthday,-3,0));
                    }else if(i >= count * 0.2 && i < count * 0.5){
                        childrenInfo.setBirthday(createBirthday(birthday,0,0));
                    }else{
                        childrenInfo.setBirthday(createBirthday(birthday,0,10));
                    }
                    u.setChildren(childrenInfo);
                    users.add(u);
                }
                createHeight(users);
                createEducation(users);
                createIncome(users);
            }
        }
        return users;
    }

    /**
     * 创建身高分配个数
     * @param users
     */
    private static void createHeight(List<CommonUser> users)
    {
        boolean flag = false;
        int c = users.size() / 10;
        while(c > 0){
            List<CommonUser> subList = users.subList((c-1)*10,c*10);
            for(int i = 0; i < subList.size();i++)
            {
                ChildrenInfo childrenInfo = subList.get(i).getChildren();
                if(Const.MALE.equals(DictInfoUtil.getItemById(childrenInfo.getGender()).getDictValue()) && !flag)
                {
                    flag = true;
                }
                if(flag)
                {
                    switch(i)
                    {
                        case 0:
                            childrenInfo.setHeight(CommonUtil.randGetBetween(157,171));
                            break;
                        case 1:
                        case 2:
                            childrenInfo.setHeight(CommonUtil.randGetBetween(170,176));
                            break;
                        case 3:
                        case 4:
                        case 5:
                            childrenInfo.setHeight(CommonUtil.randGetBetween(175,181));
                            break;
                        case 6:
                        case 7:
                        case 8:
                            childrenInfo.setHeight(CommonUtil.randGetBetween(180,191));
                            break;
                        case 9:
                            childrenInfo.setHeight(CommonUtil.randGetBetween(190,201));
                            break;
                    }
                }
                else
                {
                    switch(i)
                    {
                        case 0:
                            childrenInfo.setHeight(CommonUtil.randGetBetween(150,156));
                            break;
                        case 1:
                        case 2:
                            childrenInfo.setHeight(CommonUtil.randGetBetween(156,161));
                            break;
                        case 3:
                        case 4:
                            childrenInfo.setHeight(CommonUtil.randGetBetween(160,166));
                            break;
                        case 5:
                        case 6:
                        case 7:
                            childrenInfo.setHeight(CommonUtil.randGetBetween(165,170));
                            break;
                        case 8:
                        case 9:
                            childrenInfo.setHeight(CommonUtil.randGetBetween(170,185));
                            break;
                    }
                }
            }
            c--;
        }
    }

    /**
     * 创建学历分配个数
     * @param users
     */
    private static void createEducation(List<CommonUser> users)
    {
        int c = users.size() / 10;
        while(c > 0){
            List<CommonUser> subList = users.subList((c-1)*10,c*10);
            for(int i = 0; i < subList.size();i++)
            {
                ChildrenInfo childrenInfo = subList.get(i).getChildren();
                switch(i)
                {
                    case 0:
                    case 3:
                    case 7:
                        childrenInfo.setEducation(71L);
                        break;
                    case 1:
                    case 4:
                    case 5:
                        childrenInfo.setEducation(72L);
                        break;
                    case 6:
                    case 2:
                        childrenInfo.setEducation(73L);
                        break;
                    case 8:
                        childrenInfo.setEducation(74L);
                        break;
                    case 9:
                        childrenInfo.setEducation(75L);
                        break;
                }
            }
            c--;
        }
    }

    /**
     * 创建收入分配个数
     * @param users
     */
    private static void createIncome(List<CommonUser> users)
    {
        int c = users.size() / 10;
        while(c > 0){
            List<CommonUser> subList = users.subList((c-1)*10,c*10);
            for(int i = 0; i < subList.size();i++)
            {
                ChildrenInfo childrenInfo = subList.get(i).getChildren();
                switch(i)
                {
                    case 5:
                        childrenInfo.setIncome(81L);
                        childrenInfo.setProfession(ConstantValueUtil.PROFESSION[CommonUtil.randGetBetween(0,2)]);
                        break;
                    case 2:
                    case 4:
                    case 8:
                        childrenInfo.setIncome(82L);
                        childrenInfo.setProfession(ConstantValueUtil.PROFESSION[CommonUtil.randGetBetween(0,8)]);
                        break;
                    case 0:
                    case 6:
                    case 9:
                        childrenInfo.setIncome(83L);
                        childrenInfo.setProfession(ConstantValueUtil.PROFESSION[CommonUtil.randGetBetween(2,8)]);
                        break;
                    case 1:
                    case 3:
                        childrenInfo.setIncome(84L);
                        childrenInfo.setProfession(ConstantValueUtil.PROFESSION[CommonUtil.randGetBetween(2,8)]);
                        break;
                    case 7:
                        childrenInfo.setIncome(85L);
                        childrenInfo.setProfession("私企老板");
                        break;
                }
            }
            c--;
        }
    }

    /**
     * 生成兴趣爱好信息
     * @param num
     * @return
     */
    private static String createHobby(int num)
    {
        String hobby = "";
        for(int i = 0; i < num ; i++){
            String h  = ConstantValueUtil.HOBBY[rand.nextInt(ConstantValueUtil.HOBBY.length)];
            while(hobby.contains(h)){
                h  = ConstantValueUtil.HOBBY[rand.nextInt(ConstantValueUtil.HOBBY.length)];
            }
            hobby += h + ",";
        }
        return hobby.length() == 0 ? ConstantValueUtil.HOBBY[rand.nextInt(ConstantValueUtil.HOBBY.length)] : hobby.substring(0,hobby.length() - 1);
    }

    /**
     * 生成生日信息
     * @param currentBirthday
     * @param start
     * @param end
     * @return
     */
    private static String createBirthday(String currentBirthday,int start,int end){
        String result = "";
        int month = rand.nextInt(ConstantValueUtil.MONTH_AND_DAY.length);
        String day = ConstantValueUtil.MONTH_AND_DAY[month][rand.nextInt(ConstantValueUtil.MONTH_AND_DAY[month].length)];
        String monthAndDay = (String.valueOf(month + 1).length() == 1 ? "0"+ String.valueOf(month + 1) : String.valueOf(month + 1)) + "-" +day;
        if(currentBirthday == null){
            result = "1990-" + monthAndDay;
        }else if(start == end && start == 0){
            String year = currentBirthday.split("-")[0];
            result =  year + "-" + monthAndDay;
        }else{
            Date startDate = DateUtil.getDateMinus(currentBirthday,start);
            Date endDate = DateUtil.getDateMinus(currentBirthday,end);
            long rtn = startDate.getTime() + (long)(Math.random() * (endDate.getTime() - startDate.getTime()));
            while(rtn == startDate.getTime() || rtn == endDate.getTime()){
                rtn = startDate.getTime() + (long)(Math.random() * (endDate.getTime() - startDate.getTime()));
            }
            result = DateUtil.FORMAT.format(new Date(rtn)) ;
        }
        return result;
    }

  /**
   * 创建60个假数据
   * @param condition
   * @param minus
   * @return
   */
    public List<CommonUser> createOtherUsers(RobotUserCheckConditionCriteria condition, int minus) {
        //确定生成比例，最低10个，最多60个
        List<CommonUser> users = new ArrayList<>();
        minus = minus < 10 ? 10 : minus > 60 ? 60 : minus;
        int c = minus % 10 == 0 ? minus / 10 : minus / 10 + 1;
        while(c > 0){
            users.addAll(generateTenRobotUsers(condition));
            c--;
        }
        return users;
    }

}
