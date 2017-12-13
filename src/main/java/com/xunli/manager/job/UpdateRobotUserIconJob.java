package com.xunli.manager.job;

import com.xunli.manager.cache.ColleageCache;
import com.xunli.manager.common.Const;
import com.xunli.manager.domain.specification.CommonUserUpdateIconSpecification;
import com.xunli.manager.model.ChildrenInfo;
import com.xunli.manager.model.CommonUser;
import com.xunli.manager.repository.ChildrenInfoRepository;
import com.xunli.manager.repository.CommonUserRepository;
import com.xunli.manager.util.DictInfoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by shihj on 2017/11/27.
 */
@Component
@EnableScheduling
public class UpdateRobotUserIconJob
{

    private static volatile int start_page = 0;

    private final static Random random = new Random();

    private static volatile int M_T = 1;

    private static volatile int F_T = 1;

    private static volatile  int M = 1;

    private static volatile  int F = 1;

    private final static int F1 = 32;

    private final static int F2 = 169;

    private final static int F3 = 25;

    private final static int F4 =  101;

    private final static int M1 = 37;

    private final static int M2 = 51;

    private final static int M3 = 50;

    private final static int M4 = 33;

    private final static String path = "/icon/%s";

    private Logger logger = LoggerFactory.getLogger(UpdateRobotUserIconJob.class);

    @Autowired
    private CommonUserRepository commonUserRepository;

    @Autowired
    private ChildrenInfoRepository childrenInfoRepository;


    //@Scheduled(cron = "0/20 * * * * ?")
    public void updateRobotUserIcon()
    {
        //按照用户类型和头像为空分页查出机器用户
        Pageable page = new PageRequest(0,1000);
        List<CommonUser> users = commonUserRepository.findAll(new CommonUserUpdateIconSpecification(),page).getContent();
        if(users != null && !users.isEmpty())
        {
            List<Long> parentIds = new ArrayList<>();
            users.forEach(user -> {
                parentIds.add(user.getId());
            });
            List<ChildrenInfo> childrenInfos = childrenInfoRepository.findAllByParentIdIn(parentIds);
            childrenInfos.forEach(childrenInfo -> {
                users.forEach(user -> {
                    if(user.getId().equals(childrenInfo.getParentId()))
                    {
                        user.setIcon(getIconPath(childrenInfo));
                    }
                });
            });
            commonUserRepository.save(users);
        }
        logger.info(String.format("[page = %s,users size = %s]",0,users.size()));
    }

    //@Scheduled(cron = "0/20 * * * * ?")
    public void batchUpdateUserInfo()
    {
        //分页查找所有的用户信息
        Pageable page = new PageRequest(start_page,1000);
        List<CommonUser> users = commonUserRepository.findAll(page).getContent();
        if(users != null && !users.isEmpty())
        {
            List<Long> parentIds = new ArrayList<>();
            users.forEach(user ->
            {
                parentIds.add(user.getId());
            });
            childrenInfoRepository.findAllByParentIdIn(parentIds).forEach(childrenInfo ->
            {
                users.forEach(user -> {
                    if(user.getId().equals(childrenInfo.getParentId()))
                    {
                        user.setChildren(childrenInfo);
                    }
                });
            });
            List<CommonUser> updateUserList = new ArrayList<>();
            List<ChildrenInfo> updateChildList = new ArrayList<>();
            for(CommonUser user : users)
            {
                if(user.getChildren() != null)
                {
                    switch (user.getUsertype().intValue())
                    {
                        case 1:
                            boolean update = false;
                            if(user.getIcon() == null || user.getChildren().getIcon() == null)
                            {
                                if(user.getChildren().getGender().equals(68))
                                {
                                    user.getChildren().setIcon(String.format(path,"10000.jpg"));
                                    user.setIcon(String.format(path,"10000.jpg"));
                                }
                                else
                                {
                                    user.getChildren().setIcon(String.format(path,"20000.jpg"));
                                    user.setIcon(String.format(path,"20000.jpg"));
                                }
                                update = true;
                            }
                            if(update)
                            {
                                updateUserList.add(user);
                                updateChildList.add(user.getChildren());
                            }
                            break;
                        case 80:
                            boolean flag = checkAndUpdateColleage(user.getChildren());
                            if(checkAndUpdateProfession(user.getChildren()) || flag)
                            {
                                updateChildList.add(user.getChildren());
                            }
                            break;
                    }
                }
            }
            commonUserRepository.save(updateUserList);
            childrenInfoRepository.save(updateChildList);
        }
        start_page++;
        logger.info(String.format("[page = %s,users size = %s]",start_page,users.size()));
    }

    public static String getIconPath(ChildrenInfo childrenInfo)
    {

        String iconName = "";
        if(childrenInfo != null && DictInfoUtil.isMale(childrenInfo.getGender()))
        {
            iconName = String.format(path,"2" + String.valueOf(M_T > 4 ? 4 : M_T) + fillNumber(getMale()) + ".jpg");
        }
        else
        {
            iconName = String.format(path,"1"+ String.valueOf(F_T > 4 ? 4 : F_T) + fillNumber(getFemale())+ ".jpg");
        }
        return iconName;
    }

    public static int getMale()
    {
        if(M_T > 4)
            M_T = 1;
        switch(M_T)
        {
            case 1:
                if(M <= M1)
                    return M++;
                break;
            case 2:
                if(M <= M2)
                    return M++;
                break;
            case 3:
                if(M <= M3)
                    return M++;
                break;
            case 4:
                if(M <= M4)
                    return M++;
                break;
            default:
                break;
        }

        M = 1;
        M_T ++;
        return M++;
    }

    public static int getFemale()
    {
        if(F_T > 4)
            F_T = 1;
        switch(F_T)
        {
            case 1:
                if(F <= F1)
                    return F++;
                break;
            case 2:
                if(F <= F2)
                    return F++;
                break;
            case 3:
                if(F <= F3)
                    return F++;
                break;
            case 4:
                if(F <= F4)
                    return F++;
                break;
            default:
                break;
        }

        F = 1;
        F_T ++;
        return F++;
    }

    public static  String fillNumber(int n)
    {
        int l = 3 - String.valueOf(n).length();
        String v = String.valueOf(n);
        while(l > 0 )
        {
            v = "0" + v;
            l--;
        }
        return v;
    }

    private boolean checkAndUpdateColleage(ChildrenInfo childrenInfo)
    {
        boolean flag = false;
        if(childrenInfo.getSchool() == null || childrenInfo.getSchool().equals(""))
        {
            if(childrenInfo.getEducation().longValue() == 73)
            {
                childrenInfo.setSchool(ColleageCache.commonCollege.get(random.nextInt(ColleageCache.commonCollege.size())));
                flag = true;
            }
            else if(childrenInfo.getEducation().longValue() == 74 || childrenInfo.getEducation().longValue() == 75)
            {
                childrenInfo.setSchool(ColleageCache.goodCollege.get(random.nextInt(ColleageCache.goodCollege.size())));
                flag = true;
            }
        }
        return flag;
    }

    private boolean checkAndUpdateProfession(ChildrenInfo childrenInfo)
    {
        boolean flag = false;
        String school = childrenInfo.getSchool();
        if(school != null && (childrenInfo.getProfession() == null || childrenInfo.getProfession().equals("")))
        {
            switch(childrenInfo.getCompany().intValue())
            {
                case 86:
                    childrenInfo.setProfession("公务员");
                    flag = true;
                    break;
                case 87:
                    if(school.contains("师范"))
                    {
                        childrenInfo.setProfession("教师");
                        flag = true;
                        break;
                    }
                    if(school.contains("医"))
                    {
                        childrenInfo.setProfession("医生");
                        flag = true;
                        break;
                    }
                    if(school.contains("政法"))
                    {
                        childrenInfo.setProfession("律师");
                        flag = true;
                        break;
                    }
                    break;
                case 88:
                    if(school.contains("财经"))
                    {
                        childrenInfo.setProfession("金融从业人员");
                        flag = true;
                        break;
                    }
                    break;
                case 90:
                    if(school.contains("技术") || school.contains("科技") || school.contains("理工"))
                    {
                        childrenInfo.setProfession("研发经理");
                        flag = true;
                        break;
                    }
                    break;
            }
        }

        if(childrenInfo.getIncome() != null && (childrenInfo.getProfession() == null || childrenInfo.getProfession().equals("")))
        {
            switch(childrenInfo.getIncome().intValue())
            {
                case 85:
                    if(childrenInfo.getCompany().longValue() == 90)
                    {
                        childrenInfo.setProfession(Const.PROFESSION_1[random.nextInt(2)]);
                        flag = true;
                        break;
                    }
                    else
                    {
                        childrenInfo.setProfession("企业高管");
                        flag = true;
                        break;
                    }
                case 84:
                    childrenInfo.setProfession(Const.PROFESSION_2[random.nextInt(3)]);
                    flag = true;
                    break;
                case 83:
                    childrenInfo.setProfession(Const.PROFESSION_3[random.nextInt(2)]);
                    flag = true;
                    break;
            }
        }
        return flag;
    }

}
