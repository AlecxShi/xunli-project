package com.xunli.manager.web;

import com.xunli.manager.domain.criteria.CommonUserCriteria;
import com.xunli.manager.domain.specification.CommonUserSpecification;
import com.xunli.manager.model.ChildrenBaseInfo;
import com.xunli.manager.model.ChildrenExtendInfo;
import com.xunli.manager.model.CommonUser;
import com.xunli.manager.model.DictInfo;
import com.xunli.manager.repository.ChildrenBaseInfoRepository;
import com.xunli.manager.repository.ChildrenExtendInfoRepository;
import com.xunli.manager.repository.CommonUserRepository;
import com.xunli.manager.repository.DictInfoRepository;
import com.xunli.manager.service.GenerateService;
import com.xunli.manager.util.HeaderUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.method.P;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.xunli.manager.config.Constants.ROLE_ADMIN;

/**
 * 该部分功能用于后台管理员管理所有用户
 * Created by shihj on 2017/7/25.
 */
@RestController
@RequestMapping("/system")
public class SystemUserController {

    @Resource
    private CommonUserRepository commonUserRepository;

    @Resource
    private ChildrenBaseInfoRepository childrenBaseInfoRepository;

    @Resource
    private ChildrenExtendInfoRepository childrenExtendInfoRepository;

    @Resource
    private GenerateService generateService;

    @Resource
    private DictInfoRepository dictInfoRepository;

    @RequestMapping(value = "/user",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<CommonUser> query(@ModelAttribute CommonUserCriteria criteria, @PageableDefault Pageable page)
    {
        return commonUserRepository.findAll(new CommonUserSpecification(criteria),page);
    }

    @RequestMapping(value = "/user",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    @Secured(ROLE_ADMIN)
    public ResponseEntity<CommonUser> addOrEdit(@RequestBody CommonUser user) throws URISyntaxException
    {
        if(user.getId() != null)
        {
            return update(user);
        }
        CommonUser sa = commonUserRepository.save(user);
        return ResponseEntity.created(new URI("/system/user")).headers(HeaderUtil.createEntityCreationAlert("user",sa.getId().toString())).body(sa);
    }

    @RequestMapping(value = "/user",method = RequestMethod.PUT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT)
    @Secured(ROLE_ADMIN)
    public ResponseEntity<CommonUser> update(@RequestBody CommonUser user)
    {
        return Optional.ofNullable(commonUserRepository.findOne(user.getId())).map(u -> {
            u.setLastmodified(new Date());
            u.setUsername(user.getUsername());
            u.setPassword(user.getPassword());
            return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("commonuser",u.getUsername())).body(commonUserRepository.save(u));
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * 需要级联删除子女和子女扩展信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/user/{id}",method = RequestMethod.DELETE,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    @Secured(ROLE_ADMIN)
    public ResponseEntity<Void> delete(@PathVariable List<Long> id)
    {
        List<CommonUser> users = commonUserRepository.findAllByIdIn(id);
        List<ChildrenBaseInfo> childrens = childrenBaseInfoRepository.findAllByParentIdIn(id);
        List<Long> childrenIds = new ArrayList<Long>();
        for(ChildrenBaseInfo child : childrens)
        {
            childrenIds.add(child.getId());
        }
        List<ChildrenExtendInfo> extend = childrenExtendInfoRepository.findAllByChildrenIdIn(childrenIds);
        commonUserRepository.deleteInBatch(users);
        childrenBaseInfoRepository.deleteInBatch(childrens);
        childrenExtendInfoRepository.deleteInBatch(extend);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("commonuser",id.toString())).build();
    }

    @RequestMapping(value = "/user/quick",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT)
    @Secured(ROLE_ADMIN)
    public ResponseEntity<Void> quickGenerate()
    {
        List<DictInfo> dictInfos = dictInfoRepository.findAll();
        List<CommonUser> users = commonUserRepository.save(generateService.generateRobotUser(dictInfos));
        commonUserRepository.flush();
        List<ChildrenBaseInfo> childrens = childrenBaseInfoRepository.save(generateService.generateChildrenBaseInfo(users,dictInfos));
        childrenBaseInfoRepository.flush();
        List<ChildrenExtendInfo> extendInfos = childrenExtendInfoRepository.save(generateService.generateChildrenExtendInfo(childrens,dictInfos));
        childrenExtendInfoRepository.flush();
        return ResponseEntity.ok().build();
    }

}
