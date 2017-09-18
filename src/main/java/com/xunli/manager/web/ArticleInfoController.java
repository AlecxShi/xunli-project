package com.xunli.manager.web;

import com.xunli.manager.config.Constants;
import com.xunli.manager.domain.specification.ArticleInfoSpecification;
import com.xunli.manager.enumeration.ReturnCode;
import com.xunli.manager.model.ArticleInfo;
import com.xunli.manager.model.CommonUserLogins;
import com.xunli.manager.model.RequestResult;
import com.xunli.manager.repository.ArticleInfoRepository;
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

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

import static com.xunli.manager.config.Constants.ROLE_ADMIN;

/**
 * Created by shihj on 2017/9/11.
 */
@RestController
@RequestMapping("/system")
public class ArticleInfoController {

    @Autowired
    private ArticleInfoRepository articleInfoRepository;

    @RequestMapping(value = "/article/query",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public Page<ArticleInfo> query(@PageableDefault Pageable pageable)
    {
        return articleInfoRepository.findAll(new ArticleInfoSpecification(null), pageable);
    }

    @RequestMapping(value = "/article/save",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @Secured(ROLE_ADMIN)
    public ResponseEntity<ArticleInfo> addOrEdit(@ModelAttribute ArticleInfo articleInfo)
    {
        if(articleInfo.getId() != null)
        {

        }
        else
        {

        }
        return ResponseEntity.ok(articleInfo);
    }

    @RequestMapping(value = "/article/delete",method = RequestMethod.DELETE,produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @Secured(ROLE_ADMIN)
    public ResponseEntity<Void> delete(List<Long> ids)
    {
        for(Long id : ids)
        {
            articleInfoRepository.delete(id);
        }
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/article/upload",method = RequestMethod.POST)
    public ResponseEntity<String> uploadIcon(@RequestParam("icon")MultipartFile icon,@RequestParam("id") Long id)
    {
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
                filename = "discover_"+ id +"_icon" + filename.substring(filename.lastIndexOf("."));
                Files.copy(icon.getInputStream(), Paths.get(Constants.ICON_DISCOVER_ROOT_DIR,filename), StandardCopyOption.REPLACE_EXISTING);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            return ResponseEntity.ok(filename);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
}
