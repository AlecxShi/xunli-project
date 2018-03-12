package com.xunli.manager.web;

import com.xunli.manager.domain.criteria.RobotUserCheckConditionCriteria;
import com.xunli.manager.domain.specification.RobotUserCreateSpecification;
import com.xunli.manager.enumeration.ReturnCode;
import com.xunli.manager.model.ChildrenInfo;
import com.xunli.manager.model.CommonUser;
import com.xunli.manager.model.RequestResult;
import com.xunli.manager.repository.ChildrenInfoRepository;
import com.xunli.manager.repository.CommonUserRepository;
import com.xunli.manager.service.CommonUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

/**
 * Created by shihj on 2017/11/9.
 */
@RestController
@RequestMapping("/api")
public class RobotUserController {

    @Autowired
    private CommonUserService commonUserService;

    @Resource
    private CommonUserRepository commonUserRepository;

    @Autowired
    private ChildrenInfoRepository childrenInfoRepository;

    /**
     * 假用户登录
     * @param userId
     * @param request
     * @return
     */
    @RequestMapping(value = "/fakeuser/login",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED)
    public RequestResult commonUserLogin(@RequestParam("userId") Long userId, HttpServletRequest request)
    {
        return Optional.ofNullable(commonUserRepository.findOne(userId)).map(u -> {
            return commonUserService.login(u,request);
        }).orElseGet(()->{return new RequestResult(ReturnCode.AUTH_LOGIN_FAILURE);});
    }

    /**
     * 根据两个地址确定是否需要临时创建
     * 假用户数据，以便用户能够有良好的体验
     * @param condition
     * @return
     */
    @RequestMapping(value = "/fakeuser/generate",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> checkAndCreateRobotUser(@ModelAttribute RobotUserCheckConditionCriteria condition)
    {
        if(condition != null)
        {
            List<ChildrenInfo> list = childrenInfoRepository.findAll(new RobotUserCreateSpecification(condition));
            switch (condition.getOpType())
            {
                //只传两个地址和性别
                case "1":
                    if(list.isEmpty())
                    {
                        if(!StringUtils.isEmpty(condition.getBornLocation()) && !StringUtils.isEmpty(condition.getCurrentLocation()) && condition.getGender() != null)
                        {
                            List<CommonUser> users = commonUserService.generateTenRobotUsers(condition.getBornLocation(),condition.getCurrentLocation(),condition.getGender());
                        }
                    }
                    break;
                //传了两个地址和性别,还有生日和学历信息
                case "2":
                    if(!StringUtils.isEmpty(condition.getBornLocation()) && !StringUtils.isEmpty(condition.getCurrentLocation()) && condition.getGender() != null &&
                            !StringUtils.isEmpty(condition.getBirthday()) && condition.getEducation() != null)
                    {
                        List<CommonUser> users = commonUserService.generateTwentyRobotUsers(condition.getBornLocation(),condition.getCurrentLocation(),condition.getGender(),condition.getBirthday(),condition.getEducation());
                    }
                    break;
            }
        }
        return ResponseEntity.ok().build();
    }
}
