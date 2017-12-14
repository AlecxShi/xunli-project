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
import com.xunli.manager.common.Const;
import com.xunli.manager.job.Register2TaobaoIMJob;
import com.xunli.manager.job.UpdateUserInfoForIMJob;
import com.xunli.manager.model.ChildrenInfo;
import com.xunli.manager.model.CommonUser;
import com.xunli.manager.repository.ChildrenInfoRepository;
import com.xunli.manager.repository.CommonUserRepository;
import com.xunli.manager.util.DictInfoUtil;
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

    @Value("${api.manager.imageServer.url}")
    private String imageServer;

    @Autowired
    private CommonUserRepository commonUserRepository;

    @Autowired
    private ChildrenInfoRepository childrenInfoRepository;

    @Autowired
    private UpdateUserInfoForIMJob updateUserInfoForIMJob;

    @Autowired
    private Register2TaobaoIMJob register2TaobaoIMJob;

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
                ChildrenInfo childrenInfo = user.getChildren() == null ? childrenInfoRepository.findOneByParentId(user.getId()) : user.getChildren();
                req.setUserinfos(Arrays.asList(assembleData(user,childrenInfo,info)));
                OpenimUsersAddResponse rsp = client.execute(req);
                if(rsp != null)
                {
                    logger.info(String.format("注册信息到IM,信息体为[%s],返回结果为[%s]",info.toString(),rsp.getBody()));
                    JSONObject jsonObject = new JSONObject(rsp.getBody());
                    JSONObject successResult;
                    if(jsonObject != null && jsonObject.has(Const.IM_ADD_RESPONSE) && (successResult = jsonObject.getJSONObject(Const.IM_ADD_RESPONSE)) != null)
                    {
                        JSONObject success;
                        JSONObject fail;
                        JSONObject failMsg;
                        //解析成功注册的
                        if ((success = successResult.getJSONObject(Const.IM_SUCC)) != null && success.has(Const.IM_KEY_STRING))
                        {
                            JSONArray arr = success.getJSONArray(Const.IM_KEY_STRING);
                            for (int i = 0; i < arr.length(); i++)
                            {
                                logger.info(String.format("[%s][%s][%s][%s]", "TaobaoIMService", "RegisterUser2TaobaoIM", arr.get(i), i));
                                if (!Const.TRUE.equals(user.getIfRegister()) && String.valueOf(arr.get(i)).equals(MD5Util.Encode(user.getId()))) {
                                    user.setIfRegister(Const.TRUE);
                                    commonUserRepository.save(user);
                                }
                            }
                        }
                        //解析注册失败的
                        if (successResult.has(Const.IM_FAIL) && (fail = successResult.getJSONObject(Const.IM_FAIL)) != null
                                && successResult.has(Const.IM_FAIL_MSG) && (failMsg = successResult.getJSONObject(Const.IM_FAIL_MSG)) != null)
                        {
                            JSONArray fail_arr;
                            JSONArray failMsg_arr;
                            if (fail.has(Const.IM_KEY_STRING) && (fail_arr = fail.getJSONArray(Const.IM_KEY_STRING)) != null &&
                                    failMsg.has(Const.IM_KEY_STRING) && (failMsg_arr = failMsg.getJSONArray(Const.IM_KEY_STRING)) != null &&
                                    fail_arr.length() == failMsg_arr.length()) {
                                for (int i = 0; i < fail_arr.length(); i++)
                                {
                                    if (Const.IM_DATA_EXIST.equals(String.valueOf(failMsg_arr.get(i))))
                                    {
                                        logger.info(String.format("[%s][%s][%s][%s]", "TaobaoIMService", "batchRegisterUser2TaobaoIM", fail_arr.get(i), i));
                                        if (!Const.TRUE.equals(user.getIfRegister()) && String.valueOf(fail_arr.get(i)).equals(MD5Util.Encode(user.getId()))) {
                                            user.setIfRegister(Const.TRUE);
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
            logger.info("开始更新用户信息到IM");
            try
            {
                TaobaoClient client = getClient();
                OpenimUsersAddRequest req = new OpenimUsersAddRequest();
                List<Userinfos> infos = new ArrayList<>();
                for(int i = 0; i < users.size();i++)
                {
                    CommonUser user = users.get(i);
                    ChildrenInfo childrenInfo = user.getChildren() == null ? childrenInfoRepository.findOneByParentId(user.getId()) : user.getChildren();
                    infos.add(assembleData(user,childrenInfo,new Userinfos()));
                }
                req.setUserinfos(infos);
                OpenimUsersAddResponse rsp = client.execute(req);
                if(rsp != null)
                {
                    logger.info(rsp.getBody());
                    JSONObject jsonObject = new JSONObject(rsp.getBody());
                    JSONObject successResult;
                    if(jsonObject != null && jsonObject.has(Const.IM_ADD_RESPONSE) && (successResult = jsonObject.getJSONObject(Const.IM_ADD_RESPONSE)) != null)
                    {
                        JSONObject success;
                        JSONObject fail;
                        JSONObject failMsg;
                        //解析成功注册的
                        if((success = successResult.getJSONObject(Const.IM_SUCC)) != null && success.has(Const.IM_KEY_STRING))
                        {
                            JSONArray arr = success.getJSONArray(Const.IM_KEY_STRING);
                            for(int i = 0; i < arr.length();i++)
                            {
                                logger.info(String.format("[%s][%s][%s][%s]","TaobaoIMService","batchRegisterUser2TaobaoIM",arr.get(i),i));
                                for(CommonUser user : users)
                                {
                                    if(!Const.TRUE.equals(user.getIfRegister()) && String.valueOf(arr.get(i)).equals(MD5Util.Encode(user.getId())))
                                    {
                                        user.setIfRegister(Const.TRUE);
                                        commonUserRepository.save(user);
                                    }
                                }
                            }
                        }
                        //解析注册失败的
                        if(successResult.has(Const.IM_FAIL) && (fail = successResult.getJSONObject(Const.IM_FAIL)) != null
                                && successResult.has(Const.IM_FAIL_MSG) && (failMsg = successResult.getJSONObject(Const.IM_FAIL_MSG)) != null)
                        {
                            JSONArray fail_arr;
                            JSONArray failMsg_arr;
                            if(fail.has(Const.IM_KEY_STRING) && (fail_arr = fail.getJSONArray(Const.IM_KEY_STRING)) != null &&
                                    failMsg.has(Const.IM_KEY_STRING) && (failMsg_arr = failMsg.getJSONArray(Const.IM_KEY_STRING)) != null &&
                                    fail_arr.length() == failMsg_arr.length())
                            {
                                for(int i = 0; i < fail_arr.length();i++)
                                {
                                    if(Const.IM_DATA_EXIST.equals(String.valueOf(failMsg_arr.get(i))))
                                    {
                                        logger.info(String.format("[%s][%s][%s][%s]","TaobaoIMService","batchRegisterUser2TaobaoIM",fail_arr.get(i),i));
                                        for(CommonUser user : users)
                                        {
                                            if(!Const.TRUE.equals(user.getIfRegister()) && String.valueOf(fail_arr.get(i)).equals(MD5Util.Encode(user.getId())))
                                            {
                                                user.setIfRegister(Const.TRUE);
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
            logger.info("用户信息更新完毕");
        }
        return flag;
    }

    /**
     * 更新用户信息到IM
     * @param user
     * @return
     */
    public Boolean updateUserInfo2TaobaoIM(CommonUser user)
    {
        boolean flag = false;
        if(user != null)
        {
            TaobaoClient client = getClient();
            OpenimUsersUpdateRequest req = new OpenimUsersUpdateRequest();
            Userinfos info = new Userinfos();
            ChildrenInfo childrenInfo = user.getChildren() == null ? childrenInfoRepository.findOneByParentId(user.getId()) : user.getChildren();
            req.setUserinfos(Arrays.asList(assembleData(user,childrenInfo,info)));
            try
            {
                OpenimUsersUpdateResponse rsp = client.execute(req);
                if(rsp != null)
                {
                    logger.info(String.format("更新用户[%s]信息到IM,请求信息为[%s],收到的返回信息为[%s]",String.valueOf(user.getId()),info.toString(),rsp.getBody()));
                    JSONObject jsonObject = new JSONObject(rsp.getBody());
                    JSONObject successResult;
                    JSONObject fail;
                    JSONObject failMsg;
                    if(jsonObject != null && jsonObject.has(Const.IM_UPDATE_RESPONSE) && (successResult = jsonObject.getJSONObject(Const.IM_UPDATE_RESPONSE)) != null)
                    {
                        //解析注册失败的
                        if(successResult.has(Const.IM_FAIL) && (fail = successResult.getJSONObject(Const.IM_FAIL)) != null
                                && successResult.has(Const.IM_FAIL_MSG) && (failMsg = successResult.getJSONObject(Const.IM_FAIL_MSG)) != null)
                        {
                            JSONArray fail_arr;
                            JSONArray failMsg_arr;
                            if(fail.has(Const.IM_KEY_STRING) && (fail_arr = fail.getJSONArray(Const.IM_KEY_STRING)) != null &&
                                    failMsg.has(Const.IM_KEY_STRING) && (failMsg_arr = failMsg.getJSONArray(Const.IM_KEY_STRING)) != null &&
                                    fail_arr.length() == failMsg_arr.length())
                            {
                                for(int i = 0; i < fail_arr.length();i++)
                                {
                                    //更新不存在的用户信息时重新推送至注册任务列表
                                    switch (String.valueOf(failMsg_arr.get(i)))
                                    {
                                        case Const.IM_USER_NOT_EXIST:
                                            logger.info(String.format("[%s][%s][%s][%s]","TaobaoIMService","updateUser2TaobaoIM",Const.IM_USER_NOT_EXIST,i));
                                            register2TaobaoIMJob.push(user);
                                            break;
                                    }
                                }
                            }
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

    /**
     * 批量更新信息到IM
     * @param users
     * @return
     */
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
                ChildrenInfo childrenInfo = user.getChildren() == null ? childrenInfoRepository.findOneByParentId(user.getId()) : user.getChildren();
                infos.add(assembleData(user,childrenInfo,new Userinfos()));
            }
            req.setUserinfos(infos);
            try
            {
                OpenimUsersUpdateResponse rsp = client.execute(req);
                logger.info(String.format("更新用户信息到IM,请求信息为[%s],收到的返回信息为[%s]",infos.toString(),rsp.getBody()));
                if(rsp != null)
                {
                    logger.info(rsp.getBody());
                    JSONObject jsonObject = new JSONObject(rsp.getBody());
                    JSONObject successResult;
                    JSONObject fail;
                    JSONObject failMsg;
                    if(jsonObject != null && jsonObject.has(Const.IM_UPDATE_RESPONSE) && (successResult = jsonObject.getJSONObject(Const.IM_UPDATE_RESPONSE)) != null)
                    {
                        //解析更新失败的
                        if(successResult.has(Const.IM_FAIL) && (fail = successResult.getJSONObject(Const.IM_FAIL)) != null
                                && successResult.has(Const.IM_FAIL_MSG) && (failMsg = successResult.getJSONObject(Const.IM_FAIL_MSG)) != null)
                        {
                            JSONArray fail_arr;
                            JSONArray failMsg_arr;
                            if(fail.has(Const.IM_KEY_STRING) && (fail_arr = fail.getJSONArray(Const.IM_KEY_STRING)) != null &&
                                    failMsg.has(Const.IM_KEY_STRING) && (failMsg_arr = failMsg.getJSONArray(Const.IM_KEY_STRING)) != null &&
                                    fail_arr.length() == failMsg_arr.length())
                            {
                                for(int i = 0; i < fail_arr.length();i++)
                                {
                                    //更新不存在的用户信息时重新推送至注册任务列表
                                    for(CommonUser user : users)
                                    {
                                        if(MD5Util.Encode(user.getId()).equals(String.valueOf(fail_arr.get(i))))
                                        {
                                            switch (String.valueOf(failMsg_arr.get(i)))
                                            {
                                                case Const.IM_USER_NOT_EXIST:
                                                    logger.info(String.format("[%s][%s][%s][%s]","TaobaoIMService","updateUser2TaobaoIM",Const.IM_USER_NOT_EXIST,i));
                                                    register2TaobaoIMJob.push(user);
                                                    break;
                                                default:
                                                    updateUserInfoForIMJob.push(user);
                                                    break;
                                            }
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

    /**
     * 共用组装注册或更新到IM的信息
     * @param user
     * @param childrenInfo
     * @param info
     * @return
     */
    private Userinfos assembleData(CommonUser user,ChildrenInfo childrenInfo,Userinfos info)
    {
        info.setUserid(MD5Util.Encode(user.getId()));
        //设置密码
        info.setPassword(user.getPassword());
        //放入一些信息
        childrenInfo = user.getChildren() == null ? childrenInfoRepository.findOneByParentId(user.getId()) : user.getChildren();
        if(childrenInfo != null)
        {
            info.setName(childrenInfo.getName());
            info.setNick(childrenInfo.getName());
            //无icon信息的话使用默认头像
            if(childrenInfo.getIcon() == null)
            {
                if(DictInfoUtil.isMale(childrenInfo.getGender()))
                {
                    info.setIconUrl(String.format("%s%s",imageServer, Const.DEFAULT_MALE_ICON));
                    childrenInfo.setIcon(Const.DEFAULT_MALE_ICON);
                }
                else
                {
                    info.setIconUrl(String.format("%s%s",imageServer, Const.DEFAULT_FEMALE_ICON));
                    childrenInfo.setIcon(Const.DEFAULT_FEMALE_ICON);
                }
                childrenInfoRepository.saveAndFlush(childrenInfo);
            }
            else
            {
                info.setIconUrl(String.format("%s%s",imageServer,childrenInfo.getIcon()));
            }
        }
        info.setExtra(user.getId().toString());
        return info;
    }
}
