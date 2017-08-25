package com.xunli.manager.web;

import com.xunli.manager.model.CommonUser;
import com.xunli.manager.model.Validation;
import com.xunli.manager.model.ValidationResult;
import com.xunli.manager.repository.CommonUserRepository;
import com.xunli.manager.repository.DictInfoRepository;
import com.xunli.manager.service.CommonUserService;
import com.xunli.manager.service.GenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

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

    @RequestMapping("/getuser")
    public CommonUser getOneUser(Long id)
    {
        return commonUserService.getAll(1L);
    }

    @RequestMapping(value = "/commonuser/register",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> userRegister(@RequestBody @Valid String phone)
    {
        Validation validation = new Validation();
        validation.setId(null);
        validation.setValue(phone);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> sendCheckCode(@RequestBody @Valid String phone)
    {

        return ResponseEntity.ok().build();
    }


}
