package com.xunli.manager.web;

import com.xunli.manager.model.ChildrenInfo;
import com.xunli.manager.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by shihj on 2017/12/22.
 */
@RestController
@RequestMapping("/api/test")
public class QuickDelForTestController {

    @Autowired
    private CommonUserRepository commonUserRepository;

    @Autowired
    private ChildrenInfoRepository childrenInfoRepository;

    @Autowired
    private CommonUserLoginsRepository commonUserLoginsRepository;

    @Autowired
    private RecommendInfoTwoRepository recommendInfoTwoRepository;

    @RequestMapping(value = "/deleteByPhone",method = RequestMethod.DELETE,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<Void> deleteByPhone(@RequestParam(required = true,name = "phone") String phone)
    {
        return commonUserRepository.findOneByPhone(phone).map(user -> {
            //查到子女信息
            ChildrenInfo childrenInfo = childrenInfoRepository.findOneByParentId(user.getId());
            //删除子女信息和推荐信息
            if(childrenInfo != null)
            {
                recommendInfoTwoRepository.deleteByChildrenIdOrTargetChildrenId(childrenInfo.getId(),childrenInfo.getId());
                childrenInfoRepository.delete(childrenInfo.getId());
            }
            //删除登录信息
            commonUserLoginsRepository.deleteByUserId(user.getId());
            commonUserRepository.delete(user.getId());
            return ResponseEntity.ok().build();
        }).orElseGet(() -> {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        });
    }
}
