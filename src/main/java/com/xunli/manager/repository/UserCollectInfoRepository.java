package com.xunli.manager.repository;

import com.xunli.manager.model.UserCollectInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by shihj on 2017/9/7.
 */
public interface UserCollectInfoRepository extends JpaSpecificationExecutor<UserCollectInfo>,JpaRepository<UserCollectInfo,Long> {

    UserCollectInfo findOneByUserIdAndTargetUserId(Long userId, Long targetUserId);
}

