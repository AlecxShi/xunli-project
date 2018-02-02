package com.xunli.manager.service;

import com.xunli.manager.common.Const;
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


    public List<CommonUser> generateTenRobotUsers(String bornLocation, String currentLocation, Long gender)
    {
        List<CommonUser> users = new ArrayList<>();
        int num = 10;
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
                    ChildrenInfo childrenInfo = new ChildrenInfo();
                    childrenInfo.setBornLocation(bornLocation);
                    childrenInfo.setCurrentLocation(currentLocation);
                    childrenInfo.setName(username);
                    childrenInfo.setGender(68L);
                    childrenInfo.setIcon(UpdateRobotUserIconJob.getIconPath(childrenInfo));
                    if(i % 2 == 0)
                    {
                        childrenInfo.setCar(162L);
                    }
                    else
                    {
                        childrenInfo.setCar(163L);
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
                for(int i = 0; i < num;i++)
                {
                    CommonUser u = new CommonUser();
                    u.setIfRegister("N");
                    u.setUsertype(80L);
                    u.setLocation(bornLocation);
                    String username = ConstantValueUtil.FIRST_NAME_LIST[rand.nextInt(ConstantValueUtil.FIRST_NAME_LIST.length)] + "女士";
                    u.setUsername(username);
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
        for(int i = 0; i < users.size();i++)
        {
            ChildrenInfo childrenInfo = users.get(i).getChildren();
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
    }

    /**
     * 创建学历分配个数
     * @param users
     */
    private static void createEducation(List<CommonUser> users)
    {
        for(int i = 0; i < users.size();i++)
        {
            ChildrenInfo childrenInfo = users.get(i).getChildren();
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
    }

    /**
     * 创建收入分配个数
     * @param users
     */
    private static void createIncome(List<CommonUser> users)
    {
        for(int i = 0; i < users.size();i++)
        {
            ChildrenInfo childrenInfo = users.get(i).getChildren();
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
    }

}
