package com.xunli.manager.job;

import com.xunli.manager.domain.criteria.RobotUserCheckConditionCriteria;
import com.xunli.manager.domain.specification.RobotUserCreateSpecification;
import com.xunli.manager.model.ChildrenInfo;
import com.xunli.manager.model.CommonUser;
import com.xunli.manager.repository.ChildrenInfoRepository;
import com.xunli.manager.repository.CommonUserRepository;
import com.xunli.manager.service.CommonUserService;
import com.xunli.manager.service.GenerateService;
import com.xunli.manager.service.TaobaoIMService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@EnableScheduling
public class AutoGenerateOtherRobotUserJob {

  private final static Logger logger = LoggerFactory.getLogger(GenerateRecommendInfoJob.class);

  private BlockingQueue<ChildrenInfo> queue = new LinkedBlockingQueue<>();

  @Autowired
  private ChildrenInfoRepository childrenInfoRepository;

  @Autowired
  private CommonUserService commonUserService;

  @Autowired
  private CommonUserRepository commonUserRepository;

  @Autowired
  private TaobaoIMService taobaoIMService;

  @Autowired
  private GenerateRecommendInfoJob generateRecommendInfoJob;

  public BlockingQueue<ChildrenInfo> getQueue() {
    return queue;
  }

  public Boolean push(ChildrenInfo childrenInfo)
  {
    return queue.offer(childrenInfo);
  }

  @Scheduled(cron = "* * * * * ?")
  public void generate()
  {
    while (!queue.isEmpty())
    {
      ChildrenInfo childrenInfo = queue.poll();
      if(!StringUtils.isEmpty(childrenInfo.getCurrentLocation())
          && !StringUtils.isEmpty(childrenInfo.getBornLocation())
          && childrenInfo.getGender() != null
          && !StringUtils.isEmpty(childrenInfo.getBirthday())
          && childrenInfo.getEducation() != null){
        RobotUserCheckConditionCriteria condition = new RobotUserCheckConditionCriteria();
        condition.setBornLocation(childrenInfo.getBornLocation());
        condition.setCurrentLocation(childrenInfo.getCurrentLocation());
        condition.setGender(childrenInfo.getGender());
        condition.setEducation(childrenInfo.getEducation());
        condition.setBirthday(childrenInfo.getBirthday());
        List<ChildrenInfo> list = childrenInfoRepository.findAll(new RobotUserCreateSpecification(condition));
        if(list.size() < 60){
          int minus = 60 - list.size();
          List<CommonUser> users = commonUserService.generateOtherRobotUsers(condition,minus);
          for(CommonUser user : users){
            ChildrenInfo info = user.getChildren();
            childrenInfo.setScore(GenerateService.createScore(info));
          }
          saveAndRegister2IM(users);
          generateRecommendInfoJob.push(childrenInfo);
        }
      }
    }
  }


  @Transactional
  private void saveAndRegister2IM(List<CommonUser> users){
    if(!users.isEmpty()){
      commonUserRepository.save(users);
      List<ChildrenInfo> childrenInfos = new ArrayList<>();
      for(CommonUser user : users){
        ChildrenInfo childrenInfo = user.getChildren();
        if(user.getId() != null){
          childrenInfo.setParentId(user.getId());
          childrenInfos.add(childrenInfo);
        }
      }
      if(!childrenInfos.isEmpty()){
        childrenInfoRepository.save(childrenInfos);
        taobaoIMService.batchRegisterUser2TaobaoIM(users);
      }
      commonUserRepository.flush();
      childrenInfoRepository.flush();
    }
  }
}
