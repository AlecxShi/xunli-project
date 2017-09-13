package com.xunli.manager.web;

import com.xunli.manager.enumeration.ReturnCode;
import com.xunli.manager.model.ChildrenInfo;
import com.xunli.manager.model.CommonUserLogins;
import com.xunli.manager.model.RequestResult;
import com.xunli.manager.model.UserCollectInfo;
import com.xunli.manager.repository.ChildrenInfoRepository;
import com.xunli.manager.repository.CommonUserLoginsRepository;
import com.xunli.manager.repository.UserCollectInfoRepository;
import com.xunli.manager.util.DictInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

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

    @Autowired
    private ChildrenInfoRepository childrenInfoRepository;

    @RequestMapping(value = "/collect/save",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public RequestResult save(String token,Long targetUserId)
    {
        if(token == null || targetUserId == null)
        {
            return new RequestResult(ReturnCode.PUBLIC_PARAMETER_MISSING);
        }
        CommonUserLogins login = commonUserLoginsRepository.getByToken(token);
        if(login == null || login.getExpireTime().compareTo(new Date()) <= 0)
        {
            return new RequestResult(ReturnCode.PUBLIC_TOKEN_IS_INVALID);
        }
        Map<String,Boolean> ret = new HashMap<>();
        return Optional.ofNullable(userCollectInfoRepository.findOneByUserIdAndTargetUserId(login.getUserId(),targetUserId)).map(c -> {
            userCollectInfoRepository.delete(c.getId());
            ret.put("ifAdd",false);
            return new RequestResult(ReturnCode.PUBLIC_SUCCESS,ret);
        }).orElseGet(()->{
            UserCollectInfo collectInfo = new UserCollectInfo();
            collectInfo.setUserId(login.getUserId());
            collectInfo.setTargetUserId(targetUserId);
            userCollectInfoRepository.save(collectInfo);
            ret.put("ifAdd",true);
            return new RequestResult(ReturnCode.PUBLIC_SUCCESS,ret);
        });
    }

    @RequestMapping(value = "/collect/getAll",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public RequestResult getAll(String token)
    {
        if(token == null)
        {
            return new RequestResult(ReturnCode.PUBLIC_PARAMETER_MISSING);
        }
        CommonUserLogins login = commonUserLoginsRepository.getByToken(token);
        if(login == null || login.getExpireTime().compareTo(new Date()) <= 0)
        {
            return new RequestResult(ReturnCode.PUBLIC_TOKEN_IS_INVALID);
        }
        List<Long> parentIds = new ArrayList<>();
        for(UserCollectInfo collect : userCollectInfoRepository.findAllByUserId(login.getUserId()))
        {
            parentIds.add(collect.getTargetUserId());
        }
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        if(!parentIds.isEmpty())
        {
            for(ChildrenInfo info : childrenInfoRepository.findAllByParentIdIn(parentIds))
            {
                Map<String,Object> data = new HashMap();
                data.put("userId",info.getParentId());
                data.put("name",info.getName());
                data.put("bornLocation",info.getBornLocation());
                data.put("currentLocation",info.getCurrentLocation());
                data.put("birthday",info.getBirthday());
                data.put("education",info.getEducation());
                data.put("label", DictInfoUtil.autoAssembleLabelColor(info.getLabel().split(",")));
                list.add(data);
            }
        }
        return new RequestResult(ReturnCode.PUBLIC_SUCCESS,list);
    }
}
