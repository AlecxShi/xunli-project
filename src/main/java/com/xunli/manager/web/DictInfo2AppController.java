package com.xunli.manager.web;

import com.xunli.manager.enumeration.ReturnCode;
import com.xunli.manager.model.DictInfo;
import com.xunli.manager.model.RequestResult;
import com.xunli.manager.repository.DictInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shihj on 2017/9/4.
 */
@RestController
@RequestMapping("/api/dict")
public class DictInfo2AppController {

    @Autowired
    private DictInfoRepository dictInfoRepository;

    @RequestMapping(value = "/query",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public RequestResult getDictInfoByType()
    {
        Map<String,Object> ret = new HashMap();
        List<DictInfo> total = dictInfoRepository.findAll();
        ret.put("gender",getByDictType(total,"Gender"));
        ret.put("companyType",getByDictType(total,"Company"));
        ret.put("house",getByDictType(total,"House"));
        ret.put("car",getByDictType(total,"Car"));
        ret.put("education",getByDictType(total,"Education"));
        ret.put("income",getByDictType(total,"Income"));
        ret.put("schoolType",getByDictType(total,"School"));
        ret.put("car",getByDictType(total,"Car"));
        return new RequestResult(ReturnCode.PUBLIC_SUCCESS,ret);
    }

    private List<DictInfo> getByDictType(List<DictInfo> all,String type)
    {
        List<DictInfo> ret = new ArrayList();
        for(DictInfo dictInfo : all)
        {
            if(type.equals(dictInfo.getDictType().trim()))
            {
                ret.add(dictInfo);
            }
        }
        return ret;
    }
}
