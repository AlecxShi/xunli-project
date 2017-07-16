package com.xunli.manager.web;

import com.xunli.manager.model.CommonUser;
import com.xunli.manager.service.CommonUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Betty on 2017/7/15.
 */
@RestController
@RequestMapping("/user")
public class CommonUserController {

    @Autowired
    private CommonUserService commonUserService;

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
