package com.xunli.manager.repository;

import com.xunli.manager.model.ChildrenBaseInfo;
import com.xunli.manager.model.CommonUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by shihj on 2017/7/25.
 */
public interface ChildrenBaseInfoRepository extends JpaRepository<ChildrenBaseInfo,Long>,JpaSpecificationExecutor<ChildrenBaseInfo> {
    ChildrenBaseInfo findOneByParent(CommonUser user);
}
