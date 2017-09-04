package com.xunli.manager.web;

import com.xunli.manager.domain.criteria.RecommendInfoCriteria;
import com.xunli.manager.domain.specification.RecommendInfoSpecification;
import com.xunli.manager.enumeration.ReturnCode;
import com.xunli.manager.model.*;
import com.xunli.manager.repository.ChildrenInfoRepository;
import com.xunli.manager.repository.CommonUserLoginsRepository;
import com.xunli.manager.repository.CommonUserRepository;
import com.xunli.manager.repository.RecommendInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * Created by shihj on 2017/8/8.
 */
@RestController
@RequestMapping("/api/recommend")
public class RecommendInfoController {

    @Autowired
    private CommonUserRepository commonUserRepository;

    @Autowired
    private CommonUserLoginsRepository commonUserLoginsRepository;

    @Autowired
    private RecommendInfoRepository recommendInfoRepository;

    @Autowired
    private ChildrenInfoRepository childrenInfoRepository;

    @RequestMapping(value = "/show/login",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public RequestResult getRecommendInfo(HttpServletRequest request)
    {
        Object token = request.getParameter("token");
        Object page = request.getParameter("page");
        if(token == null || page == null)
        {
            return new RequestResult(ReturnCode.PUBLIC_PARAMETER_MISSING);
        }
        CommonUserLogins login = commonUserLoginsRepository.getByToken(token.toString());
        if(login == null || login.getExpireTime().compareTo(new Date()) <= 0)
        {
            return new RequestResult(ReturnCode.AUTH_ACCOUNT_STATUS_ERROR);
        }
        CommonUser user = commonUserRepository.findOne(login.getUserId());
        if(user == null)
        {
            return new RequestResult(ReturnCode.AUTH_ACCOUNT_IS_NULL);
        }
        ChildrenInfo childrenInfo = childrenInfoRepository.findOneByParentId(user.getId());
        if(childrenInfo == null)
        {
            return new RequestResult(ReturnCode.AUTH_ACCOUNT_IS_NULL);
        }
        PageRequest pageable = new PageRequest(Integer.parseInt(page.toString()) - 1,10);
        Page<RecommendInfo> result = recommendInfoRepository.findAll(new RecommendInfoSpecification(new RecommendInfoCriteria(childrenInfo.getId())),pageable);
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        for(RecommendInfo info : result.getContent())
        {
            Map<String,Object> data = new HashMap();
            data.put("userId",info.getTargetChildrenId().getParentId());
            data.put("name",info.getTargetChildrenId().getName());
            data.put("bornLocation",info.getTargetChildrenId().getBornLocation());
            data.put("currentLocation",info.getTargetChildrenId().getCurrentLocation());
            data.put("birthday",info.getTargetChildrenId().getBirthday());
            data.put("education",info.getTargetChildrenId().getEducation());
            list.add(data);
        }
        return new RequestResult(ReturnCode.PUBLIC_SUCCESS,list);
    }
}
