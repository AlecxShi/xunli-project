package com.xunli.manager.web;

import com.xunli.manager.codec.EncrypAES;
import com.xunli.manager.common.Const;
import com.xunli.manager.domain.criteria.CommonUserModel;
import com.xunli.manager.enumeration.ReturnCode;
import com.xunli.manager.job.Register2TaobaoIMJob;
import com.xunli.manager.model.*;
import com.xunli.manager.model.app.CommonUserWithChildrenDetail;
import com.xunli.manager.repository.*;
import com.xunli.manager.service.CommonUserService;
import com.xunli.manager.service.GenerateService;
import com.xunli.manager.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.*;

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

    @Autowired
    private RecommendInfoTwoRepository recommendInfoTwoRepository;

    @Autowired
    private UserCollectInfoRepository userCollectInfoRepository;

    @Autowired
    private Register2TaobaoIMJob register2TaobaoIMJob;

    @Value("${api.manager.imageServer.url}")
    private String imageServer;

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
            return commonUserService.login(u,request);
        }).orElseGet(() -> commonUserService.login(createUserByPhone(phone),request));
    }

    private CommonUser createUserByPhone(String phone)
    {
        CommonUser user = new CommonUser();
        user.setPhone(phone);
        user.setUsertype(DictInfoUtil.getByDictTypeAndDictValue("USER_TYPE","COMMON").getId());
        user.setUsername("");
        user.setIfRegister("N");
        CommonUtil.encrypPassword(user);
        commonUserRepository.saveAndFlush(user);
        register2TaobaoIMJob.push(user);
        return user;
    }

    @RequestMapping(value = "/commonuser/getDetail",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public RequestResult getDetail(@RequestParam("token") String token,@RequestParam(name = "targetUserId",required = false) Long targetUserId)
    {
        if(token == null)
        {
            return new RequestResult(ReturnCode.PUBLIC_TOKEN_MISSING);
        }
        CommonUserLogins login = commonUserLoginsRepository.getByToken(token);
        if(login == null || login.getExpireTime().compareTo(new Date()) <= 0)
        {
            return new RequestResult(ReturnCode.PUBLIC_USER_INFORMATION_IS_INCORRECT_OR_NO_LOGIN);
        }
        return Optional.ofNullable(commonUserRepository.findOne(login.getUserId())).map(u -> {
            return Optional.ofNullable(childrenInfoRepository.findOneByParentId(u.getId())).map(son -> {
                if(targetUserId == null)
                {
                    u.setChildren(son);
                    return new RequestResult(ReturnCode.PUBLIC_SUCCESS,transferToReturn(token,son));
                }
                return Optional.ofNullable(childrenInfoRepository.findOneByParentId(targetUserId)).map(tar -> {
                    return Optional.ofNullable(userCollectInfoRepository.findOneByUserIdAndTargetUserId(son.getParentId(),targetUserId)).map(ret -> {
                        tar.setCollected(true);
                        return new RequestResult(ReturnCode.PUBLIC_SUCCESS,transferToReturn(token,tar));
                    }).orElseGet(() -> {
                        tar.setCollected(false);
                        return new RequestResult(ReturnCode.PUBLIC_SUCCESS,transferToReturn(token,tar));
                    });
                }).orElseGet(() -> {
                    return new RequestResult(ReturnCode.AUTH_TARGET_CHILDREN_IS_NULL);
                });
            }).orElseGet(() -> {
                return new RequestResult(ReturnCode.AUTH_ACCOUNT_CHILDREN_IS_NULL);
            });
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
            u.setLastmodified(new Date());
            commonUserRepository.save(u);
            return new RequestResult(ReturnCode.PUBLIC_SUCCESS);
        }).orElseGet(() -> {
            return new RequestResult(ReturnCode.AUTH_ACCOUNT_IS_NULL);
        });
    }

    private CommonUserWithChildrenDetail transferToReturn(String token,ChildrenInfo childrenInfo)
    {
        CommonUserWithChildrenDetail ret = new CommonUserWithChildrenDetail();
        ret.setToken(token);
        ret.setId(childrenInfo.getId());
        ret.setName(childrenInfo.getName());
        ret.setGender(childrenInfo.getGender());
        ret.setBirthday(childrenInfo.getBirthday());
        ret.setHeight(childrenInfo.getHeight());
        ret.setBornLocation(childrenInfo.getBornLocation());
        ret.setCurrentLocation(childrenInfo.getCurrentLocation());

        ret.setSchoolType(childrenInfo.getSchoolType());
        ret.setSchool(childrenInfo.getSchool());
        ret.setEducation(childrenInfo.getEducation());

        ret.setParentId(childrenInfo.getParentId());

        ret.setProfession(childrenInfo.getProfession());
        ret.setCompany(childrenInfo.getCompany());
        ret.setPosition(childrenInfo.getPosition());
        ret.setIncome(childrenInfo.getIncome());

        ret.setCar(childrenInfo.getCar());
        ret.setHouse(childrenInfo.getHouse());
        if(childrenInfo.getIcon() == null)
        {
            if(ImageUtil.getUserIconByUserId(childrenInfo.getParentId()) != null)
            {
                ret.setUserImage(String.format("%s%s",imageServer,ImageUtil.getUserIconByUserId(childrenInfo.getParentId())));
            }
            else
            {
                ret.setUserImage(String.format("%s%s",imageServer,ImageUtil.getDefaultIcon(childrenInfo.getGender())));
            }
        }
        else
        {
            ret.setUserImage(String.format("%s%s",imageServer,childrenInfo.getIcon()));
        }
        List<String> photos = new ArrayList();
        if(childrenInfo.getPhoto() != null)
        {
            for(String photo : childrenInfo.getPhoto().split(Const.SPLIT))
            {
                if(photo != null && !"".equals(photo))
                {
                    photos.add(String.format("%s%s",imageServer,photo));
                }
            }
        }
        ret.setPhoto(photos);
        ret.setHobby(childrenInfo.getHobby());
        ret.setScore(childrenInfo.getScore());
        ret.setMoreIntroduce(childrenInfo.getMoreIntroduce());
        ret.setLabel(DictInfoUtil.autoAssembleLabelColor(childrenInfo.getLabel() == null ? new String[]{""} : childrenInfo.getLabel().split(",")));
        ret.setIsCollected(childrenInfo.getCollected() == null ? false : childrenInfo.getCollected());
        return ret;
    }
}
