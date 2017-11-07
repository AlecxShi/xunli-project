package com.xunli.manager.job;

import com.xunli.manager.domain.specification.CommonUser2IMSpecification;
import com.xunli.manager.model.ChildrenInfo;
import com.xunli.manager.model.CommonUser;
import com.xunli.manager.repository.ChildrenInfoRepository;
import com.xunli.manager.repository.CommonUserRepository;
import com.xunli.manager.service.TaobaoIMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
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

    @Autowired
    private ChildrenInfoRepository childrenInfoRepository;

    @Scheduled(cron = "* * * * * ?")
    public void register()
    {
        while (!queue.isEmpty())
        {
            CommonUser user = queue.poll();
            if(null == user.getChildren())
            {
                ChildrenInfo childrenInfo = childrenInfoRepository.findOneByParentId(user.getId());
                user.setChildren(childrenInfo);
            }
            //更新失败重新进入更新队列
            if(!taobaoIMService.updateUserInfo2TaobaoIM(user))
            {
                queue.offer(user);
            }
        }
    }

    @Scheduled(cron = "0/10 * * * * ?")
    public void batchRegister()
    {
        //每次注册100个账号
        Pageable page = new PageRequest(0,100);
        List<CommonUser> users = commonUserRepository.findAll(new CommonUser2IMSpecification(),page).getContent();
        taobaoIMService.batchRegisterUser2TaobaoIM(users);
    }

    public Boolean push(CommonUser commonUser)
    {
        return queue.offer(commonUser);
    }
}
