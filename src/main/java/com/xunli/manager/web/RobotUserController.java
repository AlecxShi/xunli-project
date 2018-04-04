package com.xunli.manager.web;

import com.xunli.manager.domain.criteria.RobotUserCheckConditionCriteria;
import com.xunli.manager.domain.specification.RobotUserCreateSpecification;
import com.xunli.manager.enumeration.ReturnCode;
import com.xunli.manager.job.Register2TaobaoIMJob;
import com.xunli.manager.job.UpdateRobotUserIconJob;
import com.xunli.manager.model.ChildrenInfo;
import com.xunli.manager.model.CommonUser;
import com.xunli.manager.model.RequestResult;
import com.xunli.manager.repository.ChildrenInfoRepository;
import com.xunli.manager.repository.CommonUserRepository;
import com.xunli.manager.service.CommonUserService;
import com.xunli.manager.service.GenerateService;
import com.xunli.manager.service.TaobaoIMService;
import com.xunli.manager.util.CommonUtil;
import com.xunli.manager.util.DictInfoUtil;
import java.util.ArrayList;
import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

/**
 * Created by shihj on 2017/11/9.
 */
@RestController
@RequestMapping("/api")
public class RobotUserController {

    @Autowired
    private CommonUserService commonUserService;

    @Resource
    private CommonUserRepository commonUserRepository;

    @Autowired
    private ChildrenInfoRepository childrenInfoRepository;

    @Autowired
    private TaobaoIMService taobaoIMService;

    /**
     * 假用户登录
     * @param userId
     * @param request
     * @return
     */
    @RequestMapping(value = "/fakeuser/login",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED)
    public RequestResult commonUserLogin(@RequestParam("userId") Long userId, HttpServletRequest request)
    {
        return Optional.ofNullable(commonUserRepository.findOne(userId)).map(u -> {
            return commonUserService.login(u,request);
        }).orElseGet(()->{return new RequestResult(ReturnCode.AUTH_LOGIN_FAILURE);});
    }

    /**
     * 根据两个地址确定是否需要临时创建
     * 假用户数据，以便用户能够有良好的体验
     * @param condition
     * @return
     */
    @RequestMapping(value = "/fakeuser/generate",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> checkAndCreateRobotUser(@ModelAttribute RobotUserCheckConditionCriteria condition)
    {
        if(condition != null)
        {
            List<ChildrenInfo> list = childrenInfoRepository.findAll(new RobotUserCreateSpecification(condition));
            //只处理数据小于60条的情况
            if(list.size() < 60){
                if(list.isEmpty()){
                    switch (condition.getOpType())
                    {
                        //只传两个地址和性别
                        case "1":
                            if(!StringUtils.isEmpty(condition.getBornLocation()) && !StringUtils.isEmpty(condition.getCurrentLocation()) && condition.getGender() != null)
                            {
                                condition.setBirthday(null);
                                List<CommonUser> users = commonUserService.generateTenRobotUsers(condition);
                                for(CommonUser user : users){
                                    ChildrenInfo childrenInfo = user.getChildren();
                                    childrenInfo.setScore(GenerateService.createScore(childrenInfo));
                                }
                                saveAndRegister(users);
                            }
                            break;
                        //传了两个地址和性别,还有生日和学历信息
                        case "2":
                            if(!StringUtils.isEmpty(condition.getBornLocation()) && !StringUtils.isEmpty(condition.getCurrentLocation()) && condition.getGender() != null &&
                                !StringUtils.isEmpty(condition.getBirthday()) && condition.getEducation() != null)
                            {

                                List<CommonUser> users = commonUserService.generateTenRobotUsers(condition);
                                users.addAll(commonUserService.generateTenRobotUsers(condition));
                                for(CommonUser user : users){
                                    ChildrenInfo childrenInfo = user.getChildren();
                                    childrenInfo.setScore(GenerateService.createScore(childrenInfo));
                                }
                                saveAndRegister(users);
                            }
                            break;
                    }
                }
            }
        }
        return ResponseEntity.ok().build();
    }

  /**
   * 保存数据并注册到淘宝IM
   * @param users
   */
  private void saveAndRegister(List<CommonUser> users){
        checkData(users,true);
        if(!users.isEmpty()){
            commonUserRepository.save(users);
            List<ChildrenInfo> childrenInfos = new ArrayList<>();
            for(CommonUser user : users){
                ChildrenInfo childrenInfo = user.getChildren();
                if(user.getId() != null){
                    childrenInfo.setParentId(user.getId());
                    childrenInfos.add(childrenInfo);
                }
            }
            if(!childrenInfos.isEmpty()){
                childrenInfoRepository.save(childrenInfos);
                taobaoIMService.batchRegisterUser2TaobaoIM(users);
            }
        }
    }

    /**
     * 数据校验,若指定只校验icon是否重复,则只校验icon
     * 否则,按照60条数据的校验标准进行数据校验
     * @param users
     * @param ifOnlyIcon
     */
    private void checkData(List<CommonUser> users,boolean ifOnlyIcon){
        HashSet<String> icons = new HashSet<>();
        if(ifOnlyIcon){
            for(CommonUser user : users){
                ChildrenInfo childrenInfo = user.getChildren();
                if(!icons.contains(childrenInfo.getIcon())){
                    icons.add(childrenInfo.getIcon());
                }else{
                    childrenInfo.setIcon(UpdateRobotUserIconJob.getIconPath(childrenInfo));
                    while(icons.contains(childrenInfo.getIcon())){
                        childrenInfo.setIcon(UpdateRobotUserIconJob.getIconPath(childrenInfo));
                    }
                    user.setIcon(childrenInfo.getIcon());
                }
            }
        }else{

        }
    }
}
