package com.xunli.manager.web;

import com.xunli.manager.enumeration.ReturnCode;
import com.xunli.manager.model.CommonUserLogins;
import com.xunli.manager.model.FeedBackInfo;
import com.xunli.manager.model.RequestResult;
import com.xunli.manager.repository.CommonUserLoginsRepository;
import com.xunli.manager.repository.FeedBackInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Created by shihj on 2017/9/8.
 */
@RestController
@RequestMapping("/api")
public class FeedBackInfoController {

    @Autowired
    private FeedBackInfoRepository feedBackInfoRepository;

    @Autowired
    private CommonUserLoginsRepository commonUserLoginsRepository;

    @RequestMapping(value = "/feedback/save",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public RequestResult save(@RequestParam("token")String token,@RequestParam("content") String content)
    {
        if(token == null || content == null)
        {
            return new RequestResult(ReturnCode.PUBLIC_PARAMETER_MISSING);
        }
        CommonUserLogins login = commonUserLoginsRepository.getByToken(token);
        if(login == null || login.getExpireTime().compareTo(new Date()) <= 0)
        {
            return new RequestResult(ReturnCode.PUBLIC_TOKEN_IS_INVALID);
        }
        FeedBackInfo info = new FeedBackInfo();
        info.setUserId(login.getUserId());
        info.setContent(content);
        feedBackInfoRepository.save(info);
        return new RequestResult(ReturnCode.PUBLIC_SUCCESS);
    }
}
