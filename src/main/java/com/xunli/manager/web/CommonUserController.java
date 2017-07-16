package com.xunli.manager.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Betty on 2017/7/15.
 */
@RestController
@RequestMapping("/user")
public class CommonUserController {

    @RequestMapping("/test")
    public String test()
    {
        return "test";
    }
}
