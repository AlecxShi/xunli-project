package com.xunli.manager.service;

import com.xunli.manager.domain.criteria.ChildrenInfoCriteria;
import com.xunli.manager.domain.specification.ChildrenInfoTwoSpecification;
import com.xunli.manager.enumeration.ReturnCode;
import com.xunli.manager.model.*;
import com.xunli.manager.repository.ChildrenInfoTwoRepository;
import com.xunli.manager.repository.CommonUserLoginsRepository;
import com.xunli.manager.repository.CommonUserRepository;
import com.xunli.manager.repository.RecommendInfoTwoRepository;
import com.xunli.manager.util.DictInfoUtil;
import com.xunli.manager.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
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
    private RecommendInfoTwoRepository recommendInfoTwoRepository;

    @Autowired
    private CommonUserLoginsRepository commonUserLoginsRepository;

    public CommonUser getAll(Long id)
    {
        return commonUserRepository.findOne(id);
    }

    /**
     * 该方法用于创建用户推荐信息表,在用户注册完成之后调用
     * @return
     */
    @Transactional
    public boolean generateRecommendInfo(ChildrenInfo currentChild)
    {
        boolean flag = false;
        CommonUser user = commonUserRepository.findOne(currentChild.getParentId());
        if("COMMON".equals(DictInfoUtil.getItemById(user.getUsertype()).getDictValue()))
        {
            List<ChildrenInfoTwo> top3 = generateTop3(currentChild);
            List<ChildrenInfoTwo> others = generateOthers(currentChild,top3);
            List<RecommendInfoTwo> data = new ArrayList();
            for(ChildrenInfoTwo target : top3)
            {
                RecommendInfoTwo recommendInfo = new RecommendInfoTwo();
                recommendInfo.setChildrenId(currentChild.getId());
                recommendInfo.setTargetChildrenId(target.getId());
                data.add(recommendInfo);
            }
            for(ChildrenInfoTwo target : others)
            {
                RecommendInfoTwo recommendInfo = new RecommendInfoTwo();
                recommendInfo.setChildrenId(currentChild.getId());
                recommendInfo.setTargetChildrenId(target.getId());
                data.add(recommendInfo);
            }
            recommendInfoTwoRepository.save(data);
            flag = true;
        }
        return flag;
    }

    public List<ChildrenInfoTwo> generateTop3(ChildrenInfo currentChild)
    {
        //查询所得结果
        ChildrenInfoCriteria criteria = new ChildrenInfoCriteria();
        criteria.setNum(3);
        criteria.setBornLocation(currentChild.getBornLocation());
        criteria.setCurrentLocation(currentChild.getCurrentLocation());
        criteria.setGender(DictInfoUtil.getOppositeSex(DictInfoUtil.getItemById(currentChild.getGender())));
        criteria.setEducation(DictInfoUtil.getBiggerEducation(currentChild.getEducation()));
        Long birthday = Long.parseLong(currentChild.getBirthday());
        if("Male".equals(DictInfoUtil.getItemById(currentChild.getGender()).getDictValue()))
        {
            criteria.setStartBirthday(String.valueOf(birthday - 15));
            criteria.setEndBirthday(String.valueOf(birthday + 8));
        }
        else
        {
            criteria.setStartBirthday(String.valueOf(birthday - 8));
            criteria.setEndBirthday(String.valueOf(birthday + 15));
        }
        Pageable page = new PageRequest(0,3);
        List<ChildrenInfoTwo> list = childrenInfoTwoRepository.findAll(new ChildrenInfoTwoSpecification(criteria),page).getContent();
        return list;
    }

    public List<ChildrenInfoTwo> generateOthers(ChildrenInfo currentChild,List<ChildrenInfoTwo> except)
    {
        //查询所得结果
        ChildrenInfoCriteria criteria = new ChildrenInfoCriteria();
        criteria.setNum(197);
        criteria.setBornLocation(currentChild.getBornLocation());
        criteria.setCurrentLocation(currentChild.getCurrentLocation());
        criteria.setGender(DictInfoUtil.getOppositeSex(DictInfoUtil.getItemById(currentChild.getGender())));
        criteria.setEducation(DictInfoUtil.getBiggerEducation(currentChild.getEducation()));
        Long birthday = Long.parseLong(currentChild.getBirthday());
        if("Male".equals(DictInfoUtil.getItemById(currentChild.getGender()).getDictValue()))
        {
            criteria.setStartBirthday(String.valueOf(birthday - 15));
            criteria.setEndBirthday(String.valueOf(birthday + 8));
        }
        else
        {
            criteria.setStartBirthday(String.valueOf(birthday - 8));
            criteria.setEndBirthday(String.valueOf(birthday + 15));
        }
        criteria.setExcept(except);
        Pageable page = new PageRequest(0,197);
        List<ChildrenInfoTwo> list = childrenInfoTwoRepository.findAll(new ChildrenInfoTwoSpecification(criteria),page).getContent();
        return list;
    }

    @Transactional
    public RequestResult login(CommonUser user, HttpServletRequest request)
    {
        CommonUserLogins login = commonUserLoginsRepository.findOneByUserId(user.getId());
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
        else if(login.getExpireTime().before(new Date()))
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
        Map<String,String> result = new HashMap();
        result.put("token",login.getToken());
        return new RequestResult(ReturnCode.PUBLIC_SUCCESS,result);
    }

    /*public static void main(String[] args)
    {
        Calendar instance = Calendar.getInstance();
        Date now = new Date();
        System.out.println(now);
       // instance.setTime(now);
        instance.add(Calendar.MONTH,1);
        System.out.println(instance.getTime());
    }*/
}
