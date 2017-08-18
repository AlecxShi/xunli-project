package com.xunli.manager.service;

import com.xunli.manager.model.ChildrenInfo;
import com.xunli.manager.model.CommonUser;
import com.xunli.manager.repository.CommonUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Betty on 2017/7/16.
 */
@Service("commonUserService")
public class CommonUserService {

    @Autowired
    private CommonUserRepository commonUserRepository;

    public CommonUser getAll(Long id)
    {
        return commonUserRepository.findOne(id);
    }

    /**
     * 该方法用于创建用户推荐信息表,在用户注册完成之后调用
     * @return
     */
    @Transactional
    public boolean generateRecommendInfo(ChildrenInfo currentChild)
    {
        boolean flag = false;
        generateTop3(currentChild).addAll(generateOthers(currentChild));
        return flag;
    }

    public List<ChildrenInfo> generateTop3(ChildrenInfo currentChild)
    {
        List<ChildrenInfo> result = new ArrayList();
        //查询所得结果
        return result;
    }

    public List<ChildrenInfo> generateOthers(ChildrenInfo currentChild)
    {
        List<ChildrenInfo> result = new ArrayList();
        //查询所得结果
        return result;
    }
}
