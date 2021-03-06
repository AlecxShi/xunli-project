package com.xunli.manager.web;

import com.xunli.manager.domain.criteria.ChildrenInfoCriteria;
import com.xunli.manager.domain.criteria.RecommendInfoCriteria;
import com.xunli.manager.domain.specification.ChildrenInfoThreeSpecification;
import com.xunli.manager.domain.specification.ChildrenInfoTwoSpecification;
import com.xunli.manager.domain.specification.RecommendInfoSpecification;
import com.xunli.manager.enumeration.ReturnCode;
import com.xunli.manager.model.*;
import com.xunli.manager.repository.*;
import com.xunli.manager.util.DictInfoUtil;
import com.xunli.manager.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @Value("${api.manager.imageServer.url}")
    private String imageServer;

    /**
     * 登录之后的推荐信息查询
     * @param request
     * @return
     * @auth shihj
     * @date 2017年9月6日10:06:33
     */
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
            return new RequestResult(ReturnCode.PUBLIC_USER_INFORMATION_IS_INCORRECT_OR_NO_LOGIN);
        }
        CommonUser user = commonUserRepository.findOne(login.getUserId());
        if(user == null)
        {
            return new RequestResult(ReturnCode.AUTH_ACCOUNT_IS_NULL);
        }
        ChildrenInfo childrenInfo = childrenInfoRepository.findOneByParentId(user.getId());
        if(childrenInfo == null)
        {
            return new RequestResult(ReturnCode.AUTH_ACCOUNT_CHILDREN_IS_NULL);
        }
        PageRequest pageable = new PageRequest(Integer.parseInt(page.toString()) <= 0 ? 0 : Integer.parseInt(page.toString()),10);
        Page<RecommendInfo> result = recommendInfoRepository.findAll(new RecommendInfoSpecification(new RecommendInfoCriteria(childrenInfo.getId())),pageable);
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        for(RecommendInfo info : result.getContent())
        {
            Map<String,Object> data = new HashMap();
            data.put("userId", info.getTargetChildrenId().getParentId());
            data.put("im", MD5Util.Encode(info.getTargetChildrenId().getParentId()));
            data.put("name",info.getTargetChildrenId().getName());
            if(info.getTargetChildrenId().getIcon() != null)
            {
                data.put("userImage",imageServer + info.getTargetChildrenId().getIcon());
            }
            data.put("bornLocation",info.getTargetChildrenId().getBornLocation());
            data.put("currentLocation",info.getTargetChildrenId().getCurrentLocation());
            data.put("birthday",info.getTargetChildrenId().getBirthday());
            data.put("education",info.getTargetChildrenId().getEducation());
            String label = info.getTargetChildrenId().getLabel() != null ? info.getTargetChildrenId().getLabel() : "";
            data.put("label",DictInfoUtil.autoAssembleLabelColor(label.split(",")));
            list.add(data);
        }
        return new RequestResult(ReturnCode.PUBLIC_SUCCESS,list);
    }

    /**
     * 未登录时信息查询
     * @param request
     * @return
     * @auth shihj
     * @date 2017年9月6日10:06:04
     */
    @RequestMapping(value = "/show/unlogin",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public RequestResult getNoLoginRecommendInfo(HttpServletRequest request)
    {
        Object location = request.getParameter("location");
        Object gender = request.getParameter("gender");
        Object currentLocation = request.getParameter("currentLocation");
        Object page = request.getParameter("page");
        if(location == null || currentLocation == null || gender == null || page == null)
        {
            return new RequestResult(ReturnCode.PUBLIC_PARAMETER_MISSING);
        }
        ChildrenInfoCriteria  criteria = new ChildrenInfoCriteria();
        //借用该字段判断排序
        criteria.setNum(3);
        criteria.setBornLocation(location.toString());
        criteria.setCurrentLocation(currentLocation.toString());
        criteria.setGender(DictInfoUtil.getOppositeSex(DictInfoUtil.getItemById(Long.parseLong(gender.toString()))));
        Pageable pageable = new PageRequest(Integer.parseInt(page.toString()) <= 0 ? 0 : Integer.parseInt(page.toString()),10);
        Page<ChildrenInfo> result = childrenInfoRepository.findAll(new ChildrenInfoThreeSpecification(criteria),pageable);
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        for(ChildrenInfo info : result.getContent())
        {
            Map<String,Object> data = new HashMap();
            data.put("userId",info.getParentId());
            data.put("im", MD5Util.Encode(info.getParentId()));
            data.put("name",info.getName());
            if(info.getIcon() != null)
            {
                data.put("userImage",imageServer + info.getIcon());
            }
            data.put("bornLocation",info.getBornLocation());
            data.put("currentLocation",info.getCurrentLocation());
            data.put("birthday",info.getBirthday());
            data.put("education",info.getEducation());
            String label = info.getLabel() != null ? info.getLabel() : "";
            data.put("label",DictInfoUtil.autoAssembleLabelColor(label.split(",")));
            list.add(data);
        }
        return new RequestResult(ReturnCode.PUBLIC_SUCCESS,list);
    }
}
