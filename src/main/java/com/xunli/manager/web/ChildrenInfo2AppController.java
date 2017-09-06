package com.xunli.manager.web;

import com.xunli.manager.domain.criteria.ChildrenInfoCriteria;
import com.xunli.manager.domain.criteria.UpdateChildrenInfo;
import com.xunli.manager.domain.specification.ChildrenInfoSpecification;
import com.xunli.manager.enumeration.ReturnCode;
import com.xunli.manager.model.ChildrenInfo;
import com.xunli.manager.model.CommonUser;
import com.xunli.manager.model.CommonUserLogins;
import com.xunli.manager.model.RequestResult;
import com.xunli.manager.repository.*;
import com.xunli.manager.service.CommonUserService;
import com.xunli.manager.service.GenerateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.xunli.manager.config.Constants.ROLE_ADMIN;

/**
 * Created by shihj on 2017/8/2.
 */
@RestController
@RequestMapping("/api/children")
public class ChildrenInfo2AppController {

    @Resource
    private ChildrenInfoRepository childrenInfoRepository;

    @Resource
    private CommonUserService commonUserService;

    @Resource
    private CommonUserRepository commonUserRepository;

    @Resource
    private CommonUserLoginsRepository commonUserLoginsRepository;

    @RequestMapping(value = "/save",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    public RequestResult edit(@ModelAttribute UpdateChildrenInfo childrenInfo)
    {
        if(childrenInfo.getToken() == null)
        {
            return new RequestResult(ReturnCode.PUBLIC_TOKEN_MISSING);
        }
        CommonUserLogins login = commonUserLoginsRepository.getByToken(childrenInfo.getToken());
        if(login == null || login.getExpireTime().compareTo(new Date()) <= 0)
        {
            return new RequestResult(ReturnCode.AUTH_ACCOUNT_NOT_LOGIN);
        }
        ChildrenInfo child = childrenInfoRepository.findOneByParentId(login.getUserId());
        return Optional.ofNullable(child).map( ch -> {
            if(!StringUtils.isEmpty(childrenInfo.getHobby()) && !childrenInfo.getHobby().equals(ch.getHobby()))
            {
                ch.setHobby(childrenInfo.getHobby());
            }
            if(!StringUtils.isEmpty(childrenInfo.getPosition()) && !childrenInfo.getPosition().equals(ch.getPosition()))
            {
                ch.setPosition(childrenInfo.getPosition());
            }
            if(!StringUtils.isEmpty(childrenInfo.getSchool()) && !childrenInfo.getSchool().equals(ch.getSchool()))
            {
                ch.setSchool(childrenInfo.getSchool());
            }
            if(!StringUtils.isEmpty(childrenInfo.getMoreIntroduce()) && !childrenInfo.getMoreIntroduce().equals(ch.getSchool()))
            {
                //ch.setSchool(childrenInfo.getSchool());
            }
            if(childrenInfo.getCompany() != null && !childrenInfo.getCompany().equals(ch.getCompany()))
            {
                ch.setCompany(childrenInfo.getCompany());
            }
            if(childrenInfo.getHeight() != null && !childrenInfo.getHeight().equals(ch.getHeight()))
            {
                ch.setHeight(childrenInfo.getHeight());
            }
            if(childrenInfo.getHouse() != null && !childrenInfo.getHouse().equals(ch.getHouse()))
            {
                ch.setHouse(childrenInfo.getHouse());
            }
            if(childrenInfo.getIncome() != null  && !childrenInfo.getIncome().equals(ch.getIncome()))
            {
                ch.setIncome(childrenInfo.getIncome());
            }
            if(childrenInfo.getCar() != null && !childrenInfo.getCar().equals(ch.getCar()))
            {
                ch.setCar(childrenInfo.getCar());
            }
            if(childrenInfo.getSchoolType() != null && !childrenInfo.getSchoolType().equals(ch.getSchoolType()))
            {
                ch.setSchoolType(childrenInfo.getSchoolType());
            }
            ch.setScore(GenerateService.createScore(ch));
            ch.setLabel(GenerateService.createLabel(ch));
            childrenInfoRepository.save(ch);
            //创建推荐表
            commonUserService.generateRecommendInfo(ch);
            return new RequestResult(ReturnCode.PUBLIC_SUCCESS);
        }).orElseGet(() -> {
            return new RequestResult(ReturnCode.PUBLIC_NO_DATA);
        });
    }
}
