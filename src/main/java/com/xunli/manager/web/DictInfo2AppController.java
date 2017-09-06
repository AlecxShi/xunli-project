package com.xunli.manager.web;

import com.xunli.manager.enumeration.ReturnCode;
import com.xunli.manager.model.DictInfo;
import com.xunli.manager.model.RequestResult;
import com.xunli.manager.repository.DictInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shihj on 2017/9/4.
 */
@RestController
@RequestMapping("/api/dict")
public class DictInfo2AppController {

    @Autowired
    private DictInfoRepository dictInfoRepository;

    @RequestMapping(value = "/query",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public RequestResult getDictInfoByType()
    {
         return new RequestResult(ReturnCode.PUBLIC_SUCCESS,dictInfoRepository.findAll());
    }
}
