package com.xunli.manager.web;

import com.xunli.manager.domain.criteria.ArticleInfoCriteria;
import com.xunli.manager.domain.specification.ArticleInfo2AppSpecification;
import com.xunli.manager.enumeration.ReturnCode;
import com.xunli.manager.model.ArticleInfo;
import com.xunli.manager.model.RequestResult;
import com.xunli.manager.repository.ArticleInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;

/**
 * Created by Betty on 2017/9/23.
 */
@RestController
@RequestMapping("/api")
public class ArticleInfo2AppController {

    @Autowired
    private ArticleInfoRepository articleInfoRepository;

    @Value("${api.manager.imageServer.url}")
    private String imageServer;

    @RequestMapping(value = "/article/getAll",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public RequestResult getAticleInfo(@RequestParam("page") Integer page)
    {
        Pageable pageable = new PageRequest(page == null || page <= 0 ? 0 : page,9,new Sort(new Sort.Order(Sort.Direction.DESC,"lastModified")));
        Page<ArticleInfo> result = articleInfoRepository.findAll(new ArticleInfo2AppSpecification(new ArticleInfoCriteria()),pageable);
        for(ArticleInfo articleInfo : result.getContent())
        {
            articleInfo.setImage(String.format("%s/%s",imageServer,articleInfo.getImage()));
        }
        return new RequestResult(ReturnCode.PUBLIC_SUCCESS,result.getContent());
    }
}
