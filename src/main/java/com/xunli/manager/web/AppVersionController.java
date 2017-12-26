package com.xunli.manager.web;

import com.xunli.manager.enumeration.ReturnCode;
import com.xunli.manager.model.AppVersionInfo;
import com.xunli.manager.model.RequestResult;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xunli.manager.config.Constants.ROLE_ADMIN;

/**
 * Created by shihj on 2017/12/22.
 */
@RestController
@RequestMapping("/api")
public class AppVersionController {

    @Autowired
    private AppVersionInfoRepository appVersionInfoRepository;

    @RequestMapping(value = "/version/save",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
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

    @RequestMapping(value = "/version/query",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @Secured(ROLE_ADMIN)
    public Page<AppVersionInfo> query(@PageableDefault Pageable page)
    {
        return appVersionInfoRepository.findAll(page);
    }

    @RequestMapping(value = "/version/delete",method = {RequestMethod.DELETE,RequestMethod.POST},produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @Secured(ROLE_ADMIN)
    public ResponseEntity<Void> delete(@RequestParam List<Long> ids)
    {
        for(Long id : ids)
        {
            AppVersionInfo info = appVersionInfoRepository.findOne(id);
            appVersionInfoRepository.delete(info);
        }
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/version/get",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public RequestResult getNew(@RequestParam String currentVersion)
    {
        AppVersionInfo info = appVersionInfoRepository.findTopByOrderByIdDesc();
        Map<String,Object> result = new HashMap<>();
        result.put("update",false);
        result.put("version",currentVersion);
        result.put("url","");
        if(info.getCurrentVersion().compareTo(currentVersion) > 0)
        {
            result.put("update",true);
            result.put("version",info.getCurrentVersion());
            result.put("url",info.getUrl());
        }
        return new RequestResult(ReturnCode.PUBLIC_SUCCESS,result);
    }

}
