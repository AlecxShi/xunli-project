package com.xunli.manager.web;

import com.xunli.manager.domain.criteria.RobotUserLoginCriteria;
import com.xunli.manager.domain.specification.RobotUserLoginSpecification;
import com.xunli.manager.enumeration.ReturnCode;
import com.xunli.manager.model.RequestResult;
import com.xunli.manager.model.RobotUserLogins;
import com.xunli.manager.repository.CommonUserRepository;
import com.xunli.manager.repository.RobotUserLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Date;

/**
 * Created by shihj on 2017/11/9.
 */
@RestController
@RequestMapping("/api")
public class RobotUserLoginController {

    @Autowired
    private RobotUserLoginRepository robotUserLoginRepository;

    @Autowired
    private CommonUserRepository commonUserRepository;

    @RequestMapping(value = "/fakeuser/record",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public RequestResult record(@RequestParam(name = "userId",required = true) Long userId)
    {
        return Optional.ofNullable(robotUserLoginRepository.findOne(userId)).map(u -> {
            u.setMsgCount(u.getMsgCount() + 1);
            u.setHisMsgCount(u.getHisMsgCount() + 1);
            u.setLastModified(new Date());
            robotUserLoginRepository.save(u);
            return new RequestResult(ReturnCode.PUBLIC_SUCCESS);
        }).orElseGet(() -> {
            return Optional.ofNullable(commonUserRepository.findOne(userId)).map(cu -> {
                RobotUserLogins l = new RobotUserLogins();
                l.setUserId(userId);
                l.setMsgCount(1);
                l.setHisMsgCount(0);
                l.setLastModified(new Date());
                robotUserLoginRepository.save(l);
                return new RequestResult(ReturnCode.PUBLIC_SUCCESS);
            }).orElseGet(() ->{
                return new RequestResult(ReturnCode.PUBLIC_OTHER_ERROR);
            });
        });
    }

    @RequestMapping(value = "/fakeuser/query",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public RequestResult query(@RequestParam(name ="startTime",required = true) Date startTime,
                               @RequestParam(name ="endTime",required = true) Date endTime,
                               @RequestParam(name ="page",required = true) Integer page)
    {
        RobotUserLoginCriteria criteria = new RobotUserLoginCriteria();
        criteria.setStartTime(startTime);
        criteria.setEndTime(endTime);
        Pageable p = new PageRequest(0,10);
        return new RequestResult(ReturnCode.PUBLIC_SUCCESS,robotUserLoginRepository.findAll(new RobotUserLoginSpecification(criteria),p).getContent());
    }
}
