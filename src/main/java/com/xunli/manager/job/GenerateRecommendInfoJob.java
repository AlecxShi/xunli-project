package com.xunli.manager.job;

import com.xunli.manager.domain.criteria.ChildrenInfoCriteria;
import com.xunli.manager.domain.specification.ChildrenInfoTwoSpecification;
import com.xunli.manager.model.ChildrenInfo;
import com.xunli.manager.model.ChildrenInfoTwo;
import com.xunli.manager.model.CommonUser;
import com.xunli.manager.model.RecommendInfoTwo;
import com.xunli.manager.repository.ChildrenInfoTwoRepository;
import com.xunli.manager.repository.CommonUserRepository;
import com.xunli.manager.repository.RecommendInfoTwoRepository;
import com.xunli.manager.util.DateUtil;
import com.xunli.manager.util.DictInfoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Created by shihj on 2017/9/7.
 */
@Component
@EnableScheduling
public class GenerateRecommendInfoJob {

    private final static Logger logger = LoggerFactory.getLogger(GenerateRecommendInfoJob.class);

    private BlockingQueue<ChildrenInfo> queue = new LinkedBlockingQueue<>();

    @Autowired
    private RecommendInfoTwoRepository recommendInfoTwoRepository;

    @Autowired
    private CommonUserRepository commonUserRepository;

    @Autowired
    private ChildrenInfoTwoRepository childrenInfoTwoRepository;

    @Scheduled(cron = "* * * * * ?")
    public void generate()
    {
        while (true)
        {
            while (!queue.isEmpty())
            {
                ChildrenInfo childrenInfo = queue.poll();
                //创建或重构推荐表时首先删除原来的数据
                CommonUser user = commonUserRepository.findOne(childrenInfo.getParentId());
                if("COMMON".equals(DictInfoUtil.getItemById(user.getUsertype()).getDictValue()))
                {
                    recommendInfoTwoRepository.deleteAllByChildrenId(childrenInfo.getId());
                    List<ChildrenInfoTwo> top3 = generateTop3(childrenInfo);
                    List<ChildrenInfoTwo> others = generateOthers(childrenInfo,top3);
                    List<RecommendInfoTwo> data = new ArrayList();
                    for(ChildrenInfoTwo target : top3)
                    {
                        RecommendInfoTwo recommendInfo = new RecommendInfoTwo();
                        recommendInfo.setChildrenId(childrenInfo.getId());
                        recommendInfo.setTargetChildrenId(target.getId());
                        data.add(recommendInfo);
                    }
                    for(ChildrenInfoTwo target : others)
                    {
                        RecommendInfoTwo recommendInfo = new RecommendInfoTwo();
                        recommendInfo.setChildrenId(childrenInfo.getId());
                        recommendInfo.setTargetChildrenId(target.getId());
                        data.add(recommendInfo);
                    }
                    recommendInfoTwoRepository.save(data);
                    recommendInfoTwoRepository.flush();
                }
            }
        }
    }

    @Transactional(readOnly = true)
    public List<ChildrenInfoTwo> generateTop3(ChildrenInfo currentChild)
    {
        //查询所得结果
        ChildrenInfoCriteria criteria = new ChildrenInfoCriteria();
        criteria.setNum(3);
        criteria.setBornLocation(currentChild.getBornLocation());
        criteria.setCurrentLocation(currentChild.getCurrentLocation());
        criteria.setGender(DictInfoUtil.getOppositeSex(DictInfoUtil.getItemById(currentChild.getGender())));
        criteria.setEducation(DictInfoUtil.getBiggerEducation(currentChild.getEducation()));
        if("Male".equals(DictInfoUtil.getItemById(currentChild.getGender()).getDictValue()))
        {
            criteria.setStartBirthday(DateUtil.getDate(currentChild.getBirthday(),- 15));
            criteria.setEndBirthday(DateUtil.getDate(currentChild.getBirthday(),8));
        }
        else
        {
            criteria.setStartBirthday(DateUtil.getDate(currentChild.getBirthday(), - 8));
            criteria.setEndBirthday(DateUtil.getDate(currentChild.getBirthday(),  15));
        }
        Pageable page = new PageRequest(0,3);
        List<ChildrenInfoTwo> list = childrenInfoTwoRepository.findAll(new ChildrenInfoTwoSpecification(criteria),page).getContent();
        return list;
    }

    @Transactional(readOnly = true)
    public List<ChildrenInfoTwo> generateOthers(ChildrenInfo currentChild,List<ChildrenInfoTwo> except)
    {
        //查询所得结果
        ChildrenInfoCriteria criteria = new ChildrenInfoCriteria();
        criteria.setNum(197);
        criteria.setBornLocation(currentChild.getBornLocation());
        criteria.setCurrentLocation(currentChild.getCurrentLocation());
        criteria.setGender(DictInfoUtil.getOppositeSex(DictInfoUtil.getItemById(currentChild.getGender())));
        criteria.setEducation(DictInfoUtil.getBiggerEducation(currentChild.getEducation()));
        if("Male".equals(DictInfoUtil.getItemById(currentChild.getGender()).getDictValue()))
        {
            criteria.setStartBirthday(DateUtil.getDate(currentChild.getBirthday(), - 15));
            criteria.setEndBirthday(DateUtil.getDate(currentChild.getBirthday(),  8));
        }
        else
        {
            criteria.setStartBirthday(DateUtil.getDate(currentChild.getBirthday(), - 8));
            criteria.setEndBirthday(DateUtil.getDate(currentChild.getBirthday(),15));
        }
        criteria.setExcept(except);
        Pageable page = new PageRequest(0,197);
        List<ChildrenInfoTwo> list = childrenInfoTwoRepository.findAll(new ChildrenInfoTwoSpecification(criteria),page).getContent();
        return list;
    }

    public Boolean push(ChildrenInfo childrenInfo)
    {
        return queue.offer(childrenInfo);
    }
}
