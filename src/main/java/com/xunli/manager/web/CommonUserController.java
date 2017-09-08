package com.xunli.manager.web;

import com.xunli.manager.enumeration.ReturnCode;
import com.xunli.manager.model.*;
import com.xunli.manager.repository.ChildrenInfoRepository;
import com.xunli.manager.repository.CommonUserRepository;
import com.xunli.manager.repository.DictInfoRepository;
import com.xunli.manager.service.CommonUserService;
import com.xunli.manager.service.GenerateService;
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
import javax.validation.Valid;

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

    @RequestMapping(value = "/commonuser/register",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> userRegister(@RequestBody @Valid String phone)
    {
        Validation validation = new Validation();
        validation.setId(null);
        validation.setValue(phone);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/commonuser/login",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED)
    public RequestResult commonUserLogin(@RequestParam("phone") String phone, HttpServletRequest request)
    {
        return commonUserRepository.findOneByPhone(phone).map(u -> {
            return commonUserService.login(u,request);
        }).orElseGet(() -> commonUserService.login(createUserByPhone(phone),request));
    }

    @Transactional
    private CommonUser createUserByPhone(String phone)
    {
        CommonUser user = new CommonUser();
        user.setPhone(phone);
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
}
