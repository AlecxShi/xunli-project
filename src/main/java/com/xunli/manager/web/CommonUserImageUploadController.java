package com.xunli.manager.web;

/**
 * Created by shihj on 2017/7/24.
 */

import com.xunli.manager.config.Constants;
import com.xunli.manager.enumeration.ReturnCode;
import com.xunli.manager.exception.ImageUploadException;
import com.xunli.manager.model.CommonUserLogins;
import com.xunli.manager.model.RequestResult;
import com.xunli.manager.repository.CommonUserLoginsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

@RestController
@RequestMapping("/api")
public class CommonUserImageUploadController {

    @Autowired
    private CommonUserLoginsRepository commonUserLoginsRepository;

    @RequestMapping(value = "/upload/image",method = RequestMethod.POST)
    public RequestResult uploadImage(@RequestParam("image")MultipartFile image, @RequestParam("token") String token)
    {
        CommonUserLogins login = commonUserLoginsRepository.getByToken(token);
        if(login == null || login.getExpireTime().compareTo(new Date()) <= 0)
        {
            return new RequestResult(ReturnCode.PUBLIC_TOKEN_IS_INVALID);
        }
        if(!image.isEmpty())
        {
            String filename = StringUtils.cleanPath(image.getOriginalFilename());
            try
            {
                if (filename.contains("..") ||
                        (!filename.toLowerCase().endsWith("jpg") &&
                        !filename.toLowerCase().endsWith("png") &&
                        !filename.toLowerCase().endsWith("jpeg")))
                {
                    throw new ImageUploadException(3,"Cannot store file with relative path outside current directory or not a image file "+ filename);
                }
                Files.copy(image.getInputStream(), createImageName(login.getUserId(),filename.substring(filename.lastIndexOf("."))),StandardCopyOption.REPLACE_EXISTING);
            }
            catch(ImageUploadException ex)
            {
                return new RequestResult(ex.getReturnCode());
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                return new RequestResult(ReturnCode.PUBLIC_UPLOAD_IMAGE_FAIL);
            }
            return new RequestResult(ReturnCode.PUBLIC_SUCCESS);
        }
        return new RequestResult(ReturnCode.PUBLIC_UPLOAD_IMAGE_FAIL);
    }


    @RequestMapping(value = "/upload/icon",method = RequestMethod.POST)
    public RequestResult uploadIcon(@RequestParam("icon")MultipartFile icon, @RequestParam("token") String token)
    {
        CommonUserLogins login = commonUserLoginsRepository.getByToken(token);
        if(login == null || login.getExpireTime().compareTo(new Date()) <= 0)
        {
            return new RequestResult(ReturnCode.PUBLIC_TOKEN_IS_INVALID);
        }
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
                //icon名称固定为用户编号 + _icon.icon
                filename = login.getUserId()+"_icon.png";
                Files.copy(icon.getInputStream(), Paths.get(Constants.ICON_ROOT_DIR,filename),StandardCopyOption.REPLACE_EXISTING);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                return new RequestResult(ReturnCode.PUBLIC_UPLOAD_IMAGE_FAIL);
            }
            return new RequestResult(ReturnCode.PUBLIC_SUCCESS);
        }
        return new RequestResult(ReturnCode.PUBLIC_UPLOAD_IMAGE_FAIL);
    }

    /**
     * 生成文件名
     * @param userId
     * @param fileType
     * @return
     * @throws Exception
     */
    private Path createImageName(Long userId,String fileType) throws Exception,ImageUploadException
    {
        File path = new File(Constants.IMAGE_ROOT_DIR + "/" +userId);
        if(!path.exists())
        {
            path.mkdirs();
        }
        int next;
        String filename = null;
        if(path.isDirectory())
        {
            next = path.listFiles().length + 1;
            if(next > 8)
            {
                throw  new ImageUploadException(2,"Only upload 8 pitcures");
            }
            filename = userId + "_" + next + fileType;
        }
        if(filename == null)
        {
            throw  new Exception("Create user image name error");
        }
        return Paths.get(path.getAbsolutePath()).resolve(filename);
    }

    public RequestResult deleteImage()
    {
        return  new RequestResult(ReturnCode.PUBLIC_SUCCESS);
    }

    public RequestResult deleteIcon()
    {
        return  new RequestResult(ReturnCode.PUBLIC_SUCCESS);
    }
}
