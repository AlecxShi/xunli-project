package com.xunli.manager.repository;

import com.xunli.manager.model.RecommendInfo;
import com.xunli.manager.model.RecommendInfoTwo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by shihj on 2017/8/11.
 */
@Repository
public interface RecommendInfoRepository extends JpaRepository<RecommendInfo,Long>,JpaSpecificationExecutor<RecommendInfo> {

}
