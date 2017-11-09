package com.xunli.manager.service;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Userinfos;
import com.taobao.api.request.OpenimUsersAddRequest;
import com.taobao.api.request.OpenimUsersUpdateRequest;
import com.taobao.api.response.OpenimUsersAddResponse;
import com.taobao.api.response.OpenimUsersUpdateResponse;
import com.xunli.manager.codec.EncrypAES;
import com.xunli.manager.job.UpdateUserInfoForIMJob;
import com.xunli.manager.model.ChildrenInfo;
import com.xunli.manager.model.CommonUser;
import com.xunli.manager.repository.ChildrenInfoRepository;
import com.xunli.manager.repository.CommonUserRepository;
import com.xunli.manager.util.JSONUtils;
import com.xunli.manager.util.MD5Util;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by shihj on 2017/9/26.
 */
@Service
public class TaobaoIMService {

    private final static Logger logger = LoggerFactory.getLogger(TaobaoIMService.class);

    @Value("${api.manager.im.url}")
    private String url;

    @Value("${api.manager.im.appKey}")
    private String appKey;

    @Value("${api.manager.im.appSecret}")
    private String appSecret;

    @Autowired
    private CommonUserRepository commonUserRepository;

    @Autowired
    private ChildrenInfoRepository childrenInfoRepository;

    @Autowired
    private UpdateUserInfoForIMJob updateUserInfoForIMJob;

    public TaobaoClient getClient()
    {
        TaobaoClient client = new DefaultTaobaoClient(url, appKey, appSecret);
        return client;
    }

    /**
     * 单个注册
     * @param user
     * @return
     */
    public Boolean registerUser2TaobaoIM(CommonUser user)
    {
        Boolean flag = false;
        if(user != null)
        {
            try
            {
                TaobaoClient client = getClient();
                OpenimUsersAddRequest req = new OpenimUsersAddRequest();
                Userinfos info = new Userinfos();
                //用户编号加密
                info.setUserid(MD5Util.Encode(String.valueOf(user.getId())));
                //用户密码默认已被加密
                info.setPassword(user.getPassword());
                //放入一些信息
                ChildrenInfo childrenInfo = user.getChildren() == null ? childrenInfoRepository.findOneByParentId(user.getId()) : user.getChildren();
                if(childrenInfo != null)
                {
                    info.setName(childrenInfo.getName());
                    info.setNick(childrenInfo.getName());
                }
                info.setExtra(user.getId().toString());
                req.setUserinfos(Arrays.asList(info));
                OpenimUsersAddResponse rsp = client.execute(req);
                if(rsp != null)
                {
                    System.out.println(rsp.getBody());
                    JSONObject jsonObject = new JSONObject(rsp.getBody());
                    JSONObject successResult;
                    if(jsonObject != null && jsonObject.has("openim_users_add_response") && (successResult = jsonObject.getJSONObject("openim_users_add_response")) != null)
                    {
                        JSONObject success;
                        JSONObject fail;
                        JSONObject failMsg;
                        //解析成功注册的
                        if ((success = successResult.getJSONObject("uid_succ")) != null && success.has("string"))
                        {
                            JSONArray arr = success.getJSONArray("string");
                            for (int i = 0; i < arr.length(); i++) {
                                logger.info(String.format("[%s][%s][%s][%s]", "TaobaoIMService", "batchRegisterUser2TaobaoIM", arr.get(i), i));
                                if (!"Y".equals(user.getIfRegister()) && String.valueOf(arr.get(i)).equals(MD5Util.Encode(user.getId()))) {
                                    user.setIfRegister("Y");
                                    commonUserRepository.save(user);
                                }
                            }
                        }
                        //解析注册失败的
                        if (successResult.has("uid_fail") && (fail = successResult.getJSONObject("uid_fail")) != null
                                && successResult.has("fail_msg") && (failMsg = successResult.getJSONObject("fail_msg")) != null)
                        {
                            JSONArray fail_arr;
                            JSONArray failMsg_arr;
                            if (fail.has("string") && (fail_arr = fail.getJSONArray("string")) != null &&
                                    failMsg.has("string") && (failMsg_arr = failMsg.getJSONArray("string")) != null &&
                                    fail_arr.length() == failMsg_arr.length()) {
                                for (int i = 0; i < fail_arr.length(); i++) {
                                    if ("data exist".equals(String.valueOf(failMsg_arr.get(i)))) {
                                        logger.info(String.format("[%s][%s][%s][%s]", "TaobaoIMService", "batchRegisterUser2TaobaoIM", fail_arr.get(i), i));
                                        if (!"Y".equals(user.getIfRegister()) && String.valueOf(fail_arr.get(i)).equals(MD5Util.Encode(user.getId()))) {
                                            user.setIfRegister("Y");
                                            commonUserRepository.save(user);
                                        }
                                    }
                                }
                            }
                        }
                        flag = true;
                    }
                }
            }
            catch (ApiException ex)
            {
                ex.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * 批量注册,每次注册100个账号
     * @param users
     * @return
     */
    public Boolean batchRegisterUser2TaobaoIM(List<CommonUser> users)
    {
        Boolean flag = false;
        if(users != null && !users.isEmpty())
        {
            logger.info("Start to batch register Account to Taobao IM");
            try
            {
                TaobaoClient client = getClient();
                OpenimUsersAddRequest req = new OpenimUsersAddRequest();
                List<Userinfos> infos = new ArrayList<>();
                for(int i = 0; i < users.size();i++)
                {
                    CommonUser user = users.get(i);
                    Userinfos info = new Userinfos();
                    info.setUserid(MD5Util.Encode(String.valueOf(user.getId())));
                    info.setPassword(user.getPassword());
                    //放入一些信息
                    ChildrenInfo childrenInfo = user.getChildren() == null ? childrenInfoRepository.findOneByParentId(user.getId()) : user.getChildren();
                    if(childrenInfo != null)
                    {
                        info.setName(childrenInfo.getName());
                        info.setNick(childrenInfo.getName());
                    }
                    info.setExtra(user.getId().toString());
                    infos.add(info);
                }
                req.setUserinfos(infos);
                OpenimUsersAddResponse rsp = client.execute(req);
                if(rsp != null)
                {
                    logger.info(rsp.getBody());
                    JSONObject jsonObject = new JSONObject(rsp.getBody());
                    JSONObject successResult;
                    if(jsonObject != null && jsonObject.has("openim_users_add_response") && (successResult = jsonObject.getJSONObject("openim_users_add_response")) != null)
                    {
                        JSONObject success;
                        JSONObject fail;
                        JSONObject failMsg;
                        //解析成功注册的
                        if((success = successResult.getJSONObject("uid_succ")) != null && success.has("string"))
                        {
                            JSONArray arr = success.getJSONArray("string");
                            for(int i = 0; i < arr.length();i++)
                            {
                                logger.info(String.format("[%s][%s][%s][%s]","TaobaoIMService","batchRegisterUser2TaobaoIM",arr.get(i),i));
                                for(CommonUser user : users)
                                {
                                    if(!"Y".equals(user.getIfRegister()) && String.valueOf(arr.get(i)).equals(MD5Util.Encode(user.getId())))
                                    {
                                        user.setIfRegister("Y");
                                        commonUserRepository.save(user);
                                    }
                                }
                            }
                        }
                        //解析注册失败的
                        if(successResult.has("uid_fail") && (fail = successResult.getJSONObject("uid_fail")) != null
                                && successResult.has("fail_msg") && (failMsg = successResult.getJSONObject("fail_msg")) != null)
                        {
                            JSONArray fail_arr;
                            JSONArray failMsg_arr;
                            if(fail.has("string") && (fail_arr = fail.getJSONArray("string")) != null &&
                                    failMsg.has("string") && (failMsg_arr = failMsg.getJSONArray("string")) != null &&
                                    fail_arr.length() == failMsg_arr.length())
                            {
                                for(int i = 0; i < fail_arr.length();i++)
                                {
                                    if("data exist".equals(String.valueOf(failMsg_arr.get(i))))
                                    {
                                        logger.info(String.format("[%s][%s][%s][%s]","TaobaoIMService","batchRegisterUser2TaobaoIM",fail_arr.get(i),i));
                                        for(CommonUser user : users)
                                        {
                                            if(!"Y".equals(user.getIfRegister()) && String.valueOf(fail_arr.get(i)).equals(MD5Util.Encode(user.getId())))
                                            {
                                                user.setIfRegister("Y");
                                                commonUserRepository.save(user);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        flag = true;
                    }
                }
            }
            catch (ApiException ex)
            {
                ex.printStackTrace();
            }
            logger.info("End to batch register Account to Taobao IM");
        }
        return flag;
    }

    public Boolean updateUserInfo2TaobaoIM(CommonUser user)
    {
        boolean flag = false;
        if(user != null)
        {
            TaobaoClient client = getClient();
            OpenimUsersUpdateRequest req = new OpenimUsersUpdateRequest();
            Userinfos info = new Userinfos();
            info.setUserid(MD5Util.Encode(user.getId()));
            //放入一些信息
            ChildrenInfo childrenInfo = user.getChildren() == null ? childrenInfoRepository.findOneByParentId(user.getId()) : user.getChildren();
            if(childrenInfo != null)
            {
                info.setName(childrenInfo.getName());
                info.setNick(childrenInfo.getName());
            }
            info.setExtra(user.getId().toString());
            req.setUserinfos(Arrays.asList(info));
            try
            {
                OpenimUsersUpdateResponse rsp = client.execute(req);
                if(rsp != null)
                {
                    logger.info(rsp.getBody());
                    JSONObject jsonObject = new JSONObject(rsp.getBody());
                    JSONObject successResult;
                    if(jsonObject != null && jsonObject.has("openim_users_update_response") && (successResult = jsonObject.getJSONObject("openim_users_update_response")) != null)
                    {
                        //解析更新失败的
                        if(successResult.has("uid_fail") && successResult.getJSONObject("uid_fail") != null)
                        {
                            throw new Exception("更新失败,请重试");
                        }
                        flag = true;
                    }
                }
                else
                {
                    throw new Exception("更新失败,请重试");
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        return flag;
    }

    //批量注册
    public Boolean batchUpdateUserInfo2TaobaoIM(List<CommonUser> users)
    {
        boolean flag = false;
        if(users != null && users.size() > 0)
        {
            TaobaoClient client = getClient();
            OpenimUsersUpdateRequest req = new OpenimUsersUpdateRequest();
            List<Userinfos> infos = new ArrayList();
            for(int i = 0; i < users.size();i++)
            {
                CommonUser user = users.get(i);
                Userinfos info = new Userinfos();
                info.setUserid(MD5Util.Encode(user.getId()));
                //放入一些信息
                ChildrenInfo childrenInfo = user.getChildren() == null ? childrenInfoRepository.findOneByParentId(user.getId()) : user.getChildren();
                if(childrenInfo != null)
                {
                    info.setName(childrenInfo.getName());
                    info.setNick(childrenInfo.getName());
                }
                info.setExtra(user.getId().toString());
                infos.add(info);

            }
            logger.info("update user list:" + infos.toString());
            req.setUserinfos(infos);
            try
            {
                OpenimUsersUpdateResponse rsp = client.execute(req);
                if(rsp != null)
                {
                    logger.info(rsp.getBody());
                    JSONObject jsonObject = new JSONObject(rsp.getBody());
                    JSONObject successResult;
                    if(jsonObject != null && jsonObject.has("openim_users_update_response") && (successResult = jsonObject.getJSONObject("openim_users_update_response")) != null)
                    {
                        //解析更新失败的
                        JSONObject fail;
                        if(successResult.has("uid_fail") && (fail = successResult.getJSONObject("uid_fail")) != null)
                        {
                            JSONArray fail_arr;
                            if(fail.has("string") && (fail_arr = fail.getJSONArray("string")) != null)
                            {
                                for(int i = 0; i < fail_arr.length();i++)
                                {
                                    for(CommonUser user : users)
                                    {
                                        if(MD5Util.Encode(user.getId()).equals(String.valueOf(fail_arr.get(i))))
                                        {
                                            updateUserInfoForIMJob.push(user);
                                        }
                                    }
                                }
                            }
                        }
                        flag = true;
                    }
                }
                else
                {
                    throw new Exception("更新失败");
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        return flag;
    }


}
