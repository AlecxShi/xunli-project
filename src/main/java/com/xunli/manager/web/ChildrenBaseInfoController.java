package com.xunli.manager.web;

import com.xunli.manager.domain.criteria.ChildrenBaseInfoCriteria;
import com.xunli.manager.domain.specification.ChildrenBaseInfoSpecification;
import com.xunli.manager.model.ChildrenBaseInfo;
import com.xunli.manager.model.ChildrenExtendInfo;
import com.xunli.manager.model.CommonUser;
import com.xunli.manager.repository.ChildrenBaseInfoRepository;
import com.xunli.manager.repository.ChildrenExtendInfoRepository;
import com.xunli.manager.repository.CommonUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.xunli.manager.config.Constants.ROLE_ADMIN;

/**
 * Created by shihj on 2017/7/25.
 */
@RestController
@RequestMapping("/children")
public class ChildrenBaseInfoController {

    @Resource
    private CommonUserRepository commonUserRepository;

    @Resource
    private ChildrenBaseInfoRepository childrenBaseInfoRepository;

    @Resource
    private ChildrenExtendInfoRepository childrenExtendInfoRepository;


    @RequestMapping(value = "/base",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public Page<ChildrenBaseInfo> query(@ModelAttribute ChildrenBaseInfoCriteria criteria, @PageableDefault Pageable pageable)
    {
        Page<ChildrenBaseInfo> result = childrenBaseInfoRepository.findAll(new ChildrenBaseInfoSpecification(criteria),pageable);
        for(ChildrenBaseInfo children : result)
        {
            children.setExtendInfo(childrenExtendInfoRepository.findOneByChildrenId(children.getId()));
        }
        return result;
    }

    @RequestMapping(value = "/base",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    @Secured(ROLE_ADMIN)
    public ResponseEntity<ChildrenBaseInfo> addOrEdit(@RequestBody ChildrenBaseInfo childrenBaseInfo)
    {
        if(childrenBaseInfo.getId() != null)
        {
            return update(childrenBaseInfo);
        }
        return ResponseEntity.ok().body(childrenBaseInfoRepository.save(childrenBaseInfo));
    }

    @RequestMapping(value = "/base",method = RequestMethod.PUT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    @Secured(ROLE_ADMIN)
    public ResponseEntity<ChildrenBaseInfo> update(@RequestBody ChildrenBaseInfo childrenBaseInfo)
    {
        return ResponseEntity.ok().body(childrenBaseInfoRepository.save(childrenBaseInfo));
    }

    @RequestMapping(value = "/base",method = RequestMethod.DELETE,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    @Secured(ROLE_ADMIN)
    public ResponseEntity<Void> delete(@PathVariable List<Long> id)
    {
        List<ChildrenBaseInfo> children = childrenBaseInfoRepository.findAllByIdIn(id);
        List<Long> childrenIds = new ArrayList<Long>();
        for(ChildrenBaseInfo child : children)
            childrenIds.add(child.getId());
        List<ChildrenExtendInfo> ext = childrenExtendInfoRepository.findAllByChildrenIdIn(childrenIds);
        childrenExtendInfoRepository.deleteInBatch(ext);
        childrenBaseInfoRepository.deleteInBatch(children);
        return ResponseEntity.ok().build();
    }


}
