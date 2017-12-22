package com.xunli.manager.web;

import com.xunli.manager.domain.criteria.CommonUserCriteria;
import com.xunli.manager.model.AppVersionInfo;
import com.xunli.manager.repository.AppVersionInfoRepository;
import com.xunli.manager.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

import static com.xunli.manager.config.Constants.ROLE_ADMIN;

/**
 * Created by shihj on 2017/12/22.
 */
@RestController
@RequestMapping("/api")
public class AppVersionController {

    @Autowired
    private AppVersionInfoRepository appVersionInfoRepository;

    @RequestMapping(value = "/version",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @Secured(ROLE_ADMIN)
    public ResponseEntity<AppVersionInfo> saveOrEdit(@Valid @RequestBody AppVersionInfo info)
    {
        if(info.getIfUse() == null)
        {
            info.setIfUse("N");
        }
        if(info.getCreateBy() == null)
        {
            info.setCreateBy(SecurityUtils.getCurrentUsername());
        }
        Date current = new Date();
        if(info.getCreateDate() == null)
        {
            info.setCreateDate(current);
        }
        if(info.getLastModifiedBy() == null)
        {
            info.setLastModifiedBy(SecurityUtils.getCurrentUsername());
        }
        info.setLastModified(current);
        return ResponseEntity.ok(appVersionInfoRepository.save(info));
    }

    @RequestMapping(value = "/version",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @Secured(ROLE_ADMIN)
    public Page<AppVersionInfo> query(@ModelAttribute CommonUserCriteria criteria, @PageableDefault Pageable page)
    {
        return appVersionInfoRepository.findAll(page);
    }

}
