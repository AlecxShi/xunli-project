package com.xunli.manager.web;

import com.xunli.manager.domain.criteria.ChildrenInfoModel;
import com.xunli.manager.enumeration.ReturnCode;
import com.xunli.manager.job.GenerateRecommendInfoJob;
import com.xunli.manager.model.ChildrenInfo;
import com.xunli.manager.model.CommonUserLogins;
import com.xunli.manager.model.RequestResult;
import com.xunli.manager.repository.*;
import com.xunli.manager.service.CommonUserService;
import com.xunli.manager.service.GenerateService;
import com.xunli.manager.util.DictInfoUtil;
import com.xunli.manager.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;

/**
 * Created by shihj on 2017/8/2.
 */
@RestController
@RequestMapping("/api")
public class ChildrenInfo2AppController {

    @Resource
    private ChildrenInfoRepository childrenInfoRepository;

    @Resource
    private CommonUserService commonUserService;

    @Resource
    private CommonUserRepository commonUserRepository;

    @Resource
    private CommonUserLoginsRepository commonUserLoginsRepository;

    @Autowired
    private GenerateRecommendInfoJob generateRecommendInfoJob;

    @RequestMapping(value = "/children/save",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    public RequestResult saveOrEdit(@ModelAttribute ChildrenInfoModel childrenInfo)
    {
        if(childrenInfo.getToken() == null)
        {
            return new RequestResult(ReturnCode.PUBLIC_TOKEN_MISSING);
        }
        CommonUserLogins login = commonUserLoginsRepository.getByToken(childrenInfo.getToken());
        if(login == null || login.getExpireTime().compareTo(new Date()) <= 0)
        {
            return new RequestResult(ReturnCode.PUBLIC_USER_INFORMATION_IS_INCORRECT_OR_NO_LOGIN);
        }
        ChildrenInfo child = childrenInfoRepository.findOneByParentId(login.getUserId());
        return Optional.ofNullable(child).map( ch -> {
            boolean ifReCreate = false;
            if(!StringUtils.isEmpty(childrenInfo.getHobby()) && !childrenInfo.getHobby().equals(ch.getHobby()))
            {
                ch.setHobby(childrenInfo.getHobby());
                ifReCreate = true;
            }
            if(!StringUtils.isEmpty(childrenInfo.getPosition()) && !childrenInfo.getPosition().equals(ch.getPosition()))
            {
                ch.setPosition(childrenInfo.getPosition());
            }
            if(!StringUtils.isEmpty(childrenInfo.getSchool()) && !childrenInfo.getSchool().equals(ch.getSchool()))
            {
                ch.setSchool(childrenInfo.getSchool());
            }
            if(!StringUtils.isEmpty(childrenInfo.getMoreIntroduce()) && !childrenInfo.getMoreIntroduce().equals(ch.getMoreIntroduce()))
            {
                ch.setMoreIntroduce(childrenInfo.getMoreIntroduce());
            }
            if(childrenInfo.getCompany() != null && !childrenInfo.getCompany().equals(ch.getCompany()))
            {
                ch.setCompany(childrenInfo.getCompany());
                ifReCreate = true;
            }
            if(childrenInfo.getHeight() != null && !childrenInfo.getHeight().equals(ch.getHeight()))
            {
                ch.setHeight(childrenInfo.getHeight());
            }
            if(childrenInfo.getHouse() != null && !childrenInfo.getHouse().equals(ch.getHouse()))
            {
                ch.setHouse(childrenInfo.getHouse());
                ifReCreate = true;
            }
            if(childrenInfo.getIncome() != null  && !childrenInfo.getIncome().equals(ch.getIncome()))
            {
                ch.setIncome(childrenInfo.getIncome());
                ifReCreate = true;
            }
            if(childrenInfo.getCar() != null && childrenInfo.getCar() != ch.getCar())
            {
                ch.setCar(childrenInfo.getCar());
                ifReCreate = true;
            }
            if(childrenInfo.getSchoolType() != null && !childrenInfo.getSchoolType().equals(ch.getSchoolType()))
            {
                ch.setSchoolType(childrenInfo.getSchoolType());
                ifReCreate = true;
            }
            ch.setLastmodified(new Date());
            ch.setScore(GenerateService.createScore(ch));
            childrenInfoRepository.save(ch);
            //创建推荐表
            if(ifReCreate)
            {
                generateRecommendInfoJob.push(ch);
            }
            return new RequestResult(ReturnCode.PUBLIC_SUCCESS);
        }).orElseGet(() -> {
            ChildrenInfo info = new ChildrenInfo();
            info.setName(childrenInfo.getName());
            info.setGender(childrenInfo.getGender());
            info.setBirthday(childrenInfo.getBirthday());
            info.setHeight(childrenInfo.getHeight());

            info.setBornLocation(childrenInfo.getBornLocation());
            info.setCurrentLocation(childrenInfo.getCurrentLocation());
            info.setParentId(login.getId());
            info.setCompany(childrenInfo.getCompany());
            info.setProfession(childrenInfo.getProfession());
            info.setIncome(childrenInfo.getIncome());
            info.setCar(childrenInfo.getCar());
            info.setHouse(childrenInfo.getHouse());

            info.setEducation(childrenInfo.getEducation());
            info.setSchoolType(childrenInfo.getSchoolType());
            info.setSchool(childrenInfo.getSchool());
            info.setHobby(childrenInfo.getHobby());
            info.setMoreIntroduce(childrenInfo.getMoreIntroduce());
            info.setPhoto(ImageUtil.getAllPathByUserId(login.getId()));

            info.setLabel(childrenInfo.getLabel());
            info.setScore(GenerateService.createScore(info));
            generateRecommendInfoJob.push(childrenInfoRepository.save(info));
            return new RequestResult(ReturnCode.PUBLIC_SUCCESS);
        });
    }
}
