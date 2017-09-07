package com.xunli.manager.web;

import com.xunli.manager.enumeration.ReturnCode;
import com.xunli.manager.model.CommonUserLogins;
import com.xunli.manager.model.RequestResult;
import com.xunli.manager.model.UserCollectInfo;
import com.xunli.manager.repository.CommonUserLoginsRepository;
import com.xunli.manager.repository.UserCollectInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Optional;

/**
 * Created by shihj on 2017/9/7.
 */
@RestController
@RequestMapping("/api")
public class CollectInfoController {

    @Autowired
    private CommonUserLoginsRepository commonUserLoginsRepository;

    @Autowired
    private UserCollectInfoRepository userCollectInfoRepository;

    @RequestMapping(value = "/collect/save",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public RequestResult save(String token,Long targetUserId)
    {
        if(token == null || targetUserId == null)
        {
            return new RequestResult(ReturnCode.PUBLIC_PARAMETER_MISSING);
        }
        CommonUserLogins login = commonUserLoginsRepository.getByToken(token);
        if(login.getExpireTime().compareTo(new Date()) <= 0)
        {
            return new RequestResult(ReturnCode.PUBLIC_TOKEN_IS_INVALID);
        }
        return Optional.ofNullable(userCollectInfoRepository.findOneByUserIdAndTargetUserId(login.getUserId(),targetUserId)).map(c -> {
            userCollectInfoRepository.delete(c.getId());
            return new RequestResult(ReturnCode.PUBLIC_SUCCESS);
        }).orElseGet(()->{
            UserCollectInfo collectInfo = new UserCollectInfo();
            collectInfo.setUserId(login.getUserId());
            collectInfo.setTargetUserId(targetUserId);
            userCollectInfoRepository.save(collectInfo);
            return new RequestResult(ReturnCode.PUBLIC_SUCCESS);
        });
    }
}
