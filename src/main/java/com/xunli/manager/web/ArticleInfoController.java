package com.xunli.manager.web;

import com.xunli.manager.config.Constants;
import com.xunli.manager.domain.specification.ArticleInfoSpecification;
import com.xunli.manager.enumeration.ReturnCode;
import com.xunli.manager.model.ArticleInfo;
import com.xunli.manager.model.CommonUserLogins;
import com.xunli.manager.model.RequestResult;
import com.xunli.manager.repository.ArticleInfoRepository;
import com.xunli.manager.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.xunli.manager.config.Constants.ROLE_ADMIN;

/**
 * Created by shihj on 2017/9/11.
 */
@RestController
@RequestMapping("/system")
public class ArticleInfoController {

    @Autowired
    private ArticleInfoRepository articleInfoRepository;

    private static final DateFormat FORMAT = new SimpleDateFormat("yyyyMMddhhmmssSSS");

    @RequestMapping(value = "/article/query",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public Page<ArticleInfo> query(@PageableDefault Pageable pageable)
    {
        return articleInfoRepository.findAll(new ArticleInfoSpecification(null), pageable);
    }

    @RequestMapping(value = "/article/get",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public ResponseEntity<ArticleInfo> get(@RequestParam("id") Long id)
    {
        return ResponseEntity.ok().body(articleInfoRepository.findOne(id));
    }

    @RequestMapping(value = "/article/save",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @Secured(ROLE_ADMIN)
    public ResponseEntity<ArticleInfo> addOrEdit(@RequestBody ArticleInfo articleInfo)
    {
        articleInfo.setLastModified(new Date());
        if(articleInfo.getArticleId() == null)
        {
            articleInfo.setCreateUser(SecurityUtils.getCurrentUsername());
        }
        if(articleInfo.getImage() == null || (articleInfo.getImage() != null && articleInfo.getIconName() != null && !String.format(Constants.HTTP_DISCOVER_ICON_PATH,articleInfo.getIconName()).equals(articleInfo.getImage())))
        {
            articleInfo.setImage(String.format(Constants.HTTP_DISCOVER_ICON_PATH,articleInfo.getIconName()));
        }
        articleInfoRepository.save(articleInfo);
        return ResponseEntity.ok(articleInfo);
    }

    @RequestMapping(value = "/article/publish",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @Secured(ROLE_ADMIN)
    public ResponseEntity<Void> publish(@RequestBody Map<String,Object> map)
    {
        if(map != null && map.get("ids") != null)
        {
            String ids = map.get("ids").toString();
            for(String id : ids.split(","))
            {
                ArticleInfo articleInfo = articleInfoRepository.findOne(Long.parseLong(id));
                articleInfo.setIfPublish("Y");
                articleInfo.setLastModified(new Date());
                articleInfoRepository.save(articleInfo);
            }
        }
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/article/delete",method = {RequestMethod.DELETE,RequestMethod.POST},produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @Secured(ROLE_ADMIN)
    public ResponseEntity<Void> delete(@RequestBody Map<String,Object> map)
    {
        String id1 = map.get("ids").toString();
        for(String id : id1.split(","))
        {
            ArticleInfo info = articleInfoRepository.findOne(Long.parseLong(id));
            if("N".equals(info.getIfPublish()))
            {
                articleInfoRepository.delete(info);
            }
        }
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/article/upload",method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> uploadIcon(MultipartFile icon)
    {
        Map<String,Object> result = new HashMap<>();
        if(!icon.isEmpty())
        {
            String filename = StringUtils.cleanPath(icon.getOriginalFilename());
            try
            {
                if (filename.contains("..") ||
                        (!filename.toLowerCase().endsWith("jpg") &&
                                !filename.toLowerCase().endsWith("png") &&
                                !filename.toLowerCase().endsWith("jpeg")))
                {
                    throw new Exception("Cannot store file with relative path outside current directory or not a image file "+ filename);
                }
                filename = "discover_"+ FORMAT.format(new Date()) +"_icon" + filename.substring(filename.lastIndexOf("."));
                Files.copy(icon.getInputStream(), Paths.get(Constants.ICON_DISCOVER_ROOT_DIR,filename), StandardCopyOption.REPLACE_EXISTING);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result    );
            }
            result.put("fileName",filename);
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
