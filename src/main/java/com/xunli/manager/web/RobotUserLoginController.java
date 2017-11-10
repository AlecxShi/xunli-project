package com.xunli.manager.web;

import com.xunli.manager.domain.criteria.RobotUserLoginCriteria;
import com.xunli.manager.domain.specification.RobotUserLoginSpecification;
import com.xunli.manager.enumeration.ReturnCode;
import com.xunli.manager.model.RequestResult;
import com.xunli.manager.model.RobotUserLogins;
import com.xunli.manager.repository.CommonUserRepository;
import com.xunli.manager.repository.RobotUserLoginRepository;
import com.xunli.manager.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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

    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
                l.setHisMsgCount(1);
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
    public RequestResult query(@RequestParam(name ="startTime",required = true) String startTime,
                               @RequestParam(name ="endTime",required = true) String endTime,
                               @RequestParam(name ="page",required = true) Integer page)
    {
        RobotUserLoginCriteria criteria = new RobotUserLoginCriteria();
        criteria.setStartTime(DateUtil.getDate(startTime));
        criteria.setEndTime(DateUtil.getDate(endTime));
        Pageable p = new PageRequest(page <= 0 ? 0 : page,10);
        List<Map<String,Object>> result = new ArrayList();
        for(RobotUserLogins l : robotUserLoginRepository.findAll(new RobotUserLoginSpecification(criteria),p).getContent())
        {
            Map<String,Object> map = new HashMap();
            map.put("userId",l.getUserId());
            map.put("msgCount",l.getMsgCount());
            map.put("hisMsgCount",l.getHisMsgCount());
            map.put("lastModified",DATE_FORMAT.format(l.getLastModified()));
            result.add(map);
        }
        return new RequestResult(ReturnCode.PUBLIC_SUCCESS,result);
    }
}
