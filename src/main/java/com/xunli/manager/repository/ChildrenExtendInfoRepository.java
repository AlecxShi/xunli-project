package com.xunli.manager.repository;

import com.xunli.manager.model.ChildrenBaseInfo;
import com.xunli.manager.model.ChildrenExtendInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by shihj on 2017/7/25.
 */
public interface ChildrenExtendInfoRepository extends JpaRepository<ChildrenExtendInfo,Long>,JpaSpecificationExecutor<ChildrenExtendInfo> {
    ChildrenExtendInfo findOneByChildren(ChildrenBaseInfo children);
}
