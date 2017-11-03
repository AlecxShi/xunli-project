package com.xunli.manager.job;

import com.xunli.manager.model.CommonUser;
import com.xunli.manager.repository.CommonUserRepository;
import com.xunli.manager.service.TaobaoIMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by shihj on 2017/10/31.
 */
@Component
@EnableScheduling
public class UpdateUserInfoForIMJob {

    private BlockingQueue<CommonUser> queue = new LinkedBlockingQueue<>();

    @Autowired
    private TaobaoIMService taobaoIMService;

    @Autowired
    private CommonUserRepository commonUserRepository;

    public Boolean push(CommonUser commonUser)
    {
        return queue.offer(commonUser);
    }
}
