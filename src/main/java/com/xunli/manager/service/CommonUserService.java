package com.xunli.manager.service;

import com.xunli.manager.model.CommonUser;
import com.xunli.manager.repository.CommonUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
