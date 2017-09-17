package com.xunli.manager.service;

import com.xunli.manager.domain.criteria.ChildrenInfoCriteria;
import com.xunli.manager.domain.specification.ChildrenInfoTwoSpecification;
import com.xunli.manager.enumeration.ReturnCode;
import com.xunli.manager.model.*;
import com.xunli.manager.repository.*;
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
    private ChildrenInfoRepository childrenInfoRepository;

    @Autowired
    private RecommendInfoTwoRepository recommendInfoTwoRepository;

    @Autowired
    private CommonUserLoginsRepository commonUserLoginsRepository;

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
        if(childrenInfo == null || childrenInfo.getName() == null || childrenInfo.getBirthday() == null || childrenInfo.getEducation() == null)
        {
            ifFirstLogin = true;
        }
        Map<String,Object> result = new HashMap();
        result.put("token",login.getToken());
        result.put("ifFirstLogin",ifFirstLogin);
        return new RequestResult(ReturnCode.PUBLIC_SUCCESS,result);
    }
}
