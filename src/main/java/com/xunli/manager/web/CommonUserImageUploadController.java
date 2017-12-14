package com.xunli.manager.web;

/**
 * Created by shihj on 2017/7/24.
 */

import com.xunli.manager.config.Constants;
import com.xunli.manager.enumeration.ReturnCode;
import com.xunli.manager.exception.ImageUploadException;
import com.xunli.manager.job.UpdateUserInfoForIMJob;
import com.xunli.manager.model.ChildrenInfo;
import com.xunli.manager.model.CommonUser;
import com.xunli.manager.model.CommonUserLogins;
import com.xunli.manager.model.RequestResult;
import com.xunli.manager.repository.ChildrenInfoRepository;
import com.xunli.manager.repository.CommonUserLoginsRepository;
import com.xunli.manager.repository.CommonUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CommonUserImageUploadController {

    @Autowired
    private CommonUserLoginsRepository commonUserLoginsRepository;

    @Autowired
    private ChildrenInfoRepository childrenInfoRepository;

    @Autowired
    private CommonUserRepository commonUserRepository;

    @Autowired
    private UpdateUserInfoForIMJob updateUserInfoForIMJob;

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
                Path newFile = createImageName(login.getUserId(),filename.substring(filename.lastIndexOf(".")));
                Files.copy(image.getInputStream(),newFile ,StandardCopyOption.REPLACE_EXISTING);
                ChildrenInfo childrenInfo = childrenInfoRepository.findOneByParentId(login.getUserId());
                if(childrenInfo != null)
                {
                    String photo;
                    if(childrenInfo.getPhoto() != null && !"".equals(childrenInfo.getPhoto()))
                    {
                        photo = childrenInfo.getPhoto();
                        photo = photo + "," + String.format("/image/%s/%s",login.getUserId(),newFile.getFileName());
                    }
                    else
                    {
                        photo = String.format("/image/%s/%s",login.getUserId(),newFile.getFileName());
                    }
                    childrenInfo.setPhoto(photo);
                    childrenInfoRepository.saveAndFlush(childrenInfo);
                }
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
                CommonUser user = commonUserRepository.findOne(login.getUserId());
                if(user != null)
                {
                    user.setIcon(String.format("/icon/%s",filename));
                    commonUserRepository.saveAndFlush(user);
                }
                ChildrenInfo childrenInfo = childrenInfoRepository.findOneByParentId(login.getUserId());
                if(childrenInfo != null)
                {
                    childrenInfo.setIcon(String.format("/icon/%s",filename));
                    childrenInfoRepository.saveAndFlush(childrenInfo);
                }
                updateUserInfoForIMJob.push(user);
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
