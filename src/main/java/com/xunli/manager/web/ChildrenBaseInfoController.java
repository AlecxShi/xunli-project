package com.xunli.manager.web;

import com.xunli.manager.model.ChildrenBaseInfo;
import com.xunli.manager.model.CommonUser;
import com.xunli.manager.repository.ChildrenBaseInfoRepository;
import com.xunli.manager.repository.ChildrenExtendInfoRepository;
import com.xunli.manager.repository.CommonUserRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by shihj on 2017/7/25.
 */
@RequestMapping("/children")
@RestController
public class ChildrenBaseInfoController {

    @Resource
    private CommonUserRepository commonUserRepository;

    @Resource
    private ChildrenBaseInfoRepository childrenBaseInfoRepository;

    @Resource
    private ChildrenExtendInfoRepository childrenExtendInfoRepository;

    @RequestMapping(value = "/base",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public ChildrenBaseInfo getOneByUserId(@RequestBody Long parentId)
    {
        CommonUser user = commonUserRepository.findOne(parentId);
        ChildrenBaseInfo cb = childrenBaseInfoRepository.findOneByParent(user);
        cb.setExtendInfo(childrenExtendInfoRepository.findOneByChildren(cb));
        return cb;
    }


}
