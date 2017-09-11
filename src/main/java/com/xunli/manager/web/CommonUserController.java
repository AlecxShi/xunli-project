package com.xunli.manager.web;

import com.xunli.manager.domain.criteria.CommonUserModel;
import com.xunli.manager.enumeration.ReturnCode;
import com.xunli.manager.model.*;
import com.xunli.manager.repository.ChildrenInfoRepository;
import com.xunli.manager.repository.CommonUserLoginsRepository;
import com.xunli.manager.repository.CommonUserRepository;
import com.xunli.manager.repository.DictInfoRepository;
import com.xunli.manager.service.CommonUserService;
import com.xunli.manager.service.GenerateService;
import com.xunli.manager.util.DictInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.Option;
import javax.validation.Valid;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static com.xunli.manager.config.Constants.ROLE_ADMIN;

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

    @Resource
    private CommonUserRepository commonUserRepository;

    @Autowired
    private ChildrenInfoRepository childrenInfoRepository;

    @Autowired
    private CommonUserLoginsRepository commonUserLoginsRepository;

    /**
     * 验证手机号是否已被注册
     * @param validation
     * @return
     */
    @RequestMapping(value = "/validate/commonuser/phone/unique", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured(ROLE_ADMIN)
    @Transactional(readOnly = true)
    public ValidationResult validatePhoneUnique(@RequestBody Validation validation)
    {
        return validation.getValue() == null || "".equals(validation.getValue()) ? ValidationResult.INVALID : commonUserRepository.findOneByPhone(validation.getValue()).filter(u -> validation.getId() == null ? true : !validation.getId().equals(u.getId())).map(user -> ValidationResult.INVALID).orElse(ValidationResult.VALID);

    }

    @RequestMapping(value = "/commonuser/login",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED)
    public RequestResult commonUserLogin(@RequestParam("phone") String phone, HttpServletRequest request)
    {
        return commonUserRepository.findOneByPhone(phone).map(u -> {
            return commonUserService.login(u,request,false);
        }).orElseGet(() -> commonUserService.login(createUserByPhone(phone),request,true));
    }

    @Transactional
    private CommonUser createUserByPhone(String phone)
    {
        CommonUser user = new CommonUser();
        user.setPhone(phone);
        user.setUsertype(DictInfoUtil.getByDictTypeAndDictValue("USER_TYPE","COMMON").getId());
        user.setUsername("");
        return commonUserRepository.save(user);
    }

    @RequestMapping(value = "/commonuser/getDetail",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public RequestResult getDetail(HttpServletRequest request)
    {
        String targetUserId = request.getParameter("targetUserId");
        if(targetUserId != null)
        {
            ChildrenInfo detail = childrenInfoRepository.findOneByParentId(Long.parseLong(targetUserId));
            return new RequestResult(ReturnCode.PUBLIC_SUCCESS,detail);
        }
        return new RequestResult(ReturnCode.PUBLIC_OTHER_ERROR);
    }

    @RequestMapping(value = "/commonuser/getSelfDetail",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public RequestResult getSelfDetail(@RequestParam("token") String token)
    {
        if(token == null)
        {
            return new RequestResult(ReturnCode.PUBLIC_TOKEN_MISSING);
        }
        CommonUserLogins login = commonUserLoginsRepository.getByToken(token);
        if(login == null || login.getExpireTime().compareTo(new Date()) <= 0)
        {
            return new RequestResult(ReturnCode.AUTH_ACCOUNT_NOT_LOGIN);
        }
        return Optional.ofNullable(commonUserRepository.findOne(login.getUserId())).map(u -> {
            ChildrenInfo detail = childrenInfoRepository.findOneByParentId(login.getUserId());
            u.setChildren(detail);
            return new RequestResult(ReturnCode.PUBLIC_SUCCESS,u);
        }).orElseGet(() -> {
            return new RequestResult(ReturnCode.AUTH_ACCOUNT_IS_NULL);
        });
    }

    @RequestMapping(value = "/commonuser/save",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public RequestResult update(@ModelAttribute CommonUserModel model)
    {
        if(model.getToken() == null)
        {
            return new RequestResult(ReturnCode.PUBLIC_TOKEN_MISSING);
        }
        CommonUserLogins login = commonUserLoginsRepository.getByToken(model.getToken());
        if(login == null || login.getExpireTime().compareTo(new Date()) <= 0)
        {
            return new RequestResult(ReturnCode.PUBLIC_TOKEN_IS_INVALID);
        }
        return Optional.ofNullable(commonUserRepository.findOne(login.getUserId())).map(u -> {
            if(model.getUsername() != null && !model.getUsername().equals(u.getUsername()))
            {
                u.setUsername(model.getUsername());
            }

            if(model.getLocation() != null && !model.getLocation().equals(u.getLocation()))
            {
                u.setLocation(model.getLocation());
            }

            if(model.getPassword() != null && !model.getPassword().equals(u.getPassword()))
            {
                u.setPassword(model.getPassword());
            }
            u.setLastmodified(new Date());
            commonUserRepository.save(u);
            return new RequestResult(ReturnCode.PUBLIC_SUCCESS);
        }).orElseGet(() -> {
            return new RequestResult(ReturnCode.AUTH_ACCOUNT_IS_NULL);
        });
    }
}
