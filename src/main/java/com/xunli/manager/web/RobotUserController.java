package com.xunli.manager.web;

import com.xunli.manager.domain.criteria.RobotUserCheckConditionCriteria;
import com.xunli.manager.enumeration.ReturnCode;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity<Void> checkAndCreateRobotUser(@ModelAttribute RobotUserCheckConditionCriteria condition)
    {

        return ResponseEntity.ok().build();
    }
}
