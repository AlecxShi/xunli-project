package com.xunli.manager.job;

import com.xunli.manager.domain.criteria.CommonUserCriteria;
import com.xunli.manager.domain.specification.CommonUser2IMSpecification;
import com.xunli.manager.domain.specification.CommonUserSpecification;
import com.xunli.manager.model.ChildrenInfo;
import com.xunli.manager.model.CommonUser;
import com.xunli.manager.repository.ChildrenInfoRepository;
import com.xunli.manager.repository.CommonUserRepository;
import com.xunli.manager.service.TaobaoIMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by shihj on 2017/10/31.
 */
@Component
@EnableScheduling
public class UpdateUserInfoForIMJob {

    private BlockingQueue<CommonUser> queue = new LinkedBlockingQueue<>();

    private static volatile int start_page = 0;

    @Autowired
    private TaobaoIMService taobaoIMService;

    @Autowired
    private CommonUserRepository commonUserRepository;

    @Autowired
    private ChildrenInfoRepository childrenInfoRepository;

    //@Scheduled(cron = "0/10 * * * * ?")
    public void batchUpdate()
    {
        Pageable page = new PageRequest(start_page,100);
        List<CommonUser> users = commonUserRepository.findAll(new CommonUserSpecification(new CommonUserCriteria()),page).getContent();
        List<Long> parentIds = new ArrayList<>();
        for(CommonUser user : users)
        {
            parentIds.add(user.getId());
        }
        List<ChildrenInfo> childrenInfos = childrenInfoRepository.findAllByParentIdIn(parentIds);
        for(CommonUser user : users)
        {
            for(ChildrenInfo childrenInfo : childrenInfos)
            {
                if(childrenInfo.getParentId().equals(user.getId()))
                {
                    user.setChildren(childrenInfo);
                }
            }
        }
        System.out.println(String.format("[page = %s,users size = %s]",start_page,users.size()));
        taobaoIMService.batchUpdateUserInfo2TaobaoIM(users);
        start_page++;
    }

    @Scheduled(cron = "* * * * * ?")
    public void update()
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

    public Boolean push(CommonUser commonUser)
    {
        return queue.offer(commonUser);
    }
}
