package com.xunli.manager.job;

import com.xunli.manager.domain.specification.CommonUser2IMSpecification;
import com.xunli.manager.model.ChildrenInfo;
import com.xunli.manager.model.CommonUser;
import com.xunli.manager.repository.CommonUserRepository;
import com.xunli.manager.service.CommonUserService;
import com.xunli.manager.service.TaobaoIMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by shihj on 2017/9/27.
 */
@Component
@EnableScheduling
public class Register2TaobaoIMJob {

    private BlockingQueue<CommonUser> queue = new LinkedBlockingQueue<>();

    @Autowired
    private TaobaoIMService taobaoIMService;

    @Autowired
    private CommonUserRepository commonUserRepository;

    public Boolean push(CommonUser commonUser)
    {
        return queue.offer(commonUser);
    }

    @Scheduled(cron = "* * * * * ?")
    public void register()
    {
        while (!queue.isEmpty())
        {
            CommonUser user = queue.poll();
            if(!taobaoIMService.registerUser2TaobaoIM(user))
            {
                queue.offer(user);
            }
        }
    }

    /**
     * 批量注册,扫描表中未注册到IM的用户进行注册
     * 每隔10s钟执行1次
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void batchRegister()
    {
        //每次注册100个账号
        Pageable page = new PageRequest(0,100);
        List<CommonUser> users = commonUserRepository.findAll(new CommonUser2IMSpecification(),page).getContent();
        taobaoIMService.batchRegisterUser2TaobaoIM(users);
    }

}
