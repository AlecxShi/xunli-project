package com.xunli.manager.web;

import com.xunli.manager.cache.DictInfoCache;
import com.xunli.manager.domain.criteria.DictInfoCriteria;
import com.xunli.manager.domain.specification.DictInfoSpecification;
import com.xunli.manager.model.DictInfo;
import com.xunli.manager.repository.DictInfoRepository;
import com.xunli.manager.util.HeaderUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.xunli.manager.config.Constants.ROLE_ADMIN;

/**
 * Created by shihj on 2017/7/24.
 */
@RestController
@RequestMapping("/system")
public class DictInfoController {

    @Resource
    private DictInfoRepository dictInfoRepository;

    @Resource
    private DictInfoCache dictInfoCache;

    @RequestMapping(value = "/dictinfo",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public Page<DictInfo> pageQuery(@ModelAttribute DictInfoCriteria criteria,@PageableDefault Pageable page)
    {
        return dictInfoRepository.findAll(new DictInfoSpecification(criteria),page);
    }

    @RequestMapping(path="/dictinfo",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.REPEATABLE_READ)
    @Secured(ROLE_ADMIN)
    public ResponseEntity<DictInfo> addOrUpdate(@Valid @RequestBody DictInfo dict) throws URISyntaxException {
        if(dict.getId() != null)
        {
            return update(dict);
        }
        DictInfo dc = dictInfoRepository.save(dict);
        dictInfoCache.updateCache();
        return ResponseEntity.created(new URI("/api/dictinfo")).headers(HeaderUtil.createEntityCreationAlert("dict",dc.getId().toString())).body(dc);
    }

    @RequestMapping(value="/dictinfo",method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT)
    @Secured(ROLE_ADMIN)
    public ResponseEntity<DictInfo> update(@RequestBody DictInfo dict)
    {
        return Optional.ofNullable(dictInfoRepository.findOne(dict.getId())).map(d -> {
            d.setDictType(dict.getDictType());
            d.setDictDesc(dict.getDictDesc());
            d.setDictValue(dict.getDictValue());
            dictInfoRepository.save(d);
            dictInfoCache.updateCache();
            return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("dict",d.getId().toString())).body(d);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @RequestMapping(value = "/dictinfo/{ids}",method = RequestMethod.DELETE,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT)
    @Secured(ROLE_ADMIN)
    public ResponseEntity<Void> delete(@PathVariable List<Long> ids)
    {
        for(Long id : ids)
        {
            dictInfoRepository.delete(id);
        }
        dictInfoCache.updateCache();
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("dict",ids.toString())).build();
    }

    @RequestMapping(value = "/dictinfo/getByType",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public List<DictInfo> getAllByDictType(String dictType)
    {
        return dictInfoRepository.findAllByDictType(dictType);
    }
}
