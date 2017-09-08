package com.xunli.manager.repository;

import com.xunli.manager.model.FeedBackInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by shihj on 2017/9/8.
 */
public interface FeedBackInfoRepository extends JpaRepository<FeedBackInfo,Long>,JpaSpecificationExecutor<FeedBackInfo>{

}
