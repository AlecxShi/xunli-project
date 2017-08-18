package com.xunli.manager.web;

import com.xunli.manager.domain.criteria.ChildrenInfoCriteria;
import com.xunli.manager.domain.specification.ChildrenInfoSpecification;
import com.xunli.manager.model.ChildrenInfo;
import com.xunli.manager.model.DictInfo;
import com.xunli.manager.repository.ChildrenInfoRepository;
import com.xunli.manager.repository.DictInfoRepository;
import com.xunli.manager.service.GenerateService;
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
import java.util.List;

import static com.xunli.manager.config.Constants.ROLE_ADMIN;

/**
 * Created by shihj on 2017/8/2.
 */
@RestController
@RequestMapping("/system")
public class ChildrenInfoController {

    @Resource
    private ChildrenInfoRepository childrenInfoRepository;

    @Resource
    private DictInfoRepository dictInfoRepository;

    @RequestMapping(value = "/children",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public Page<ChildrenInfo> query(@ModelAttribute ChildrenInfoCriteria criteria, @PageableDefault Pageable pageable)
    {
        return childrenInfoRepository.findAll(new ChildrenInfoSpecification(criteria),pageable);
    }

    @RequestMapping(value = "/children",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    @Secured(ROLE_ADMIN)
    public ResponseEntity<ChildrenInfo> addOrEdit(@RequestBody ChildrenInfo childrenInfo)
    {
        if(childrenInfo.getId() != null)
        {
            return update(childrenInfo);
        }
        List<DictInfo> dictInfos = dictInfoRepository.findAll();
        childrenInfo.setScore(GenerateService.createScore(childrenInfo,dictInfos));
        childrenInfo.setLabel(GenerateService.createLabel(childrenInfo,dictInfos));
        return ResponseEntity.ok().body(childrenInfoRepository.save(childrenInfo));
    }

    @RequestMapping(value = "/children",method = RequestMethod.PUT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    @Secured(ROLE_ADMIN)
    public ResponseEntity<ChildrenInfo> update(@RequestBody ChildrenInfo childrenInfo)
    {
        List<DictInfo> dictInfos = dictInfoRepository.findAll();
        childrenInfo.setScore(GenerateService.createScore(childrenInfo,dictInfos));
        childrenInfo.setLabel(GenerateService.createLabel(childrenInfo,dictInfos));
        return ResponseEntity.ok().body(childrenInfoRepository.save(childrenInfo));
    }

    @RequestMapping(value = "/children",method = RequestMethod.DELETE,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    @Secured(ROLE_ADMIN)
    public ResponseEntity<Void> delete(@PathVariable List<Long> id)
    {
        List<ChildrenInfo> children = childrenInfoRepository.findAllByIdIn(id);
        childrenInfoRepository.deleteInBatch(children);
        return ResponseEntity.ok().build();
    }
}
