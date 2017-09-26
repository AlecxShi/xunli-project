package com.xunli.manager.service;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Userinfos;
import com.taobao.api.request.OpenimUsersAddRequest;
import com.taobao.api.response.OpenimUsersAddResponse;
import com.xunli.manager.codec.EncrypAES;
import com.xunli.manager.model.CommonUser;
import com.xunli.manager.util.JSONUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by shihj on 2017/9/26.
 */
@Service
public class TaobaoIMService {

    @Value("${api.manager.im.url}")
    private String url;

    @Value("${api.manager.im.appKey}")
    private String appKey;

    @Value("${api.manager.im.appSecret}")
    private String appSecret;

    public TaobaoClient getClient()
    {
        TaobaoClient client = new DefaultTaobaoClient(url, appKey, appSecret);
        return client;
    }

    public Boolean registerUser2TaobaoIM(CommonUser user)
    {
        Boolean flag = false;
        try
        {
            TaobaoClient client = getClient();
            OpenimUsersAddRequest req = new OpenimUsersAddRequest();
            Userinfos info = new Userinfos();
            //加密用户名和密码
            EncrypAES encrypAES = new EncrypAES();
            info.setUserid(encrypAES.Encrytor(String.valueOf(user.getId())));
            info.setPassword(user.getPassword());
            req.setUserinfos(Arrays.asList(info));
            OpenimUsersAddResponse rsp = client.execute(req);
            if(rsp != null)
            {
                System.out.println(rsp.getBody());
                JSONObject jsonObject = new JSONObject(rsp.getBody());
                if(jsonObject == null || jsonObject.get("openim_users_add_response") == null)
                {

                }
            }
            flag = true;
        }
        catch (ApiException |
                NoSuchAlgorithmException |
                NoSuchPaddingException |
                InvalidKeyException |
                IllegalBlockSizeException |
                BadPaddingException ex)
        {
            ex.printStackTrace();
        }
        return flag;
    }

/*    public static void main(String[] args) {
        String json = "{\"openim_users_add_response\":{\"uid_succ\":{\"string\":[\"uid\"]},\"uid_fail\":{\"string\":[\"uid\"]},\"fail_msg\":{\"string\":[\"db error\"]}}}";
        JSONObject obj = new JSONObject(json);
        System.out.println(obj.get("openim_users_add_response"));
    }*/
}
