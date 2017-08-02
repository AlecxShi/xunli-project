package com.xunli.manager.web;

import com.xunli.manager.model.CommonUser;
import com.xunli.manager.repository.DictInfoRepository;
import com.xunli.manager.service.CommonUserService;
import com.xunli.manager.service.GenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 该部分提供APP端发起注册，查询等功能
 * Created by Betty on 2017/7/15.
 */
@RestController
@RequestMapping("/api")
public class CommonUserController {

    @Autowired
    private CommonUserService commonUserService;

    @Resource
    private GenerateService generateService;

    @Resource
    private DictInfoRepository dictInfoRepository;

    @RequestMapping("/test")
    public String test()
    {
        return "test";
    }

    @RequestMapping("/getuser")
    public CommonUser getOneUser(Long id)
    {
        return commonUserService.getAll(1L);
    }


}
