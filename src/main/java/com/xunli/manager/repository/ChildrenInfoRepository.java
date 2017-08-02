package com.xunli.manager.repository;

import com.xunli.manager.model.ChildrenInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by shihj on 2017/8/2.
 */
@Repository
public interface ChildrenInfoRepository extends JpaRepository<ChildrenInfo,Long>,JpaSpecificationExecutor<ChildrenInfo> {

    List<ChildrenInfo> findAllByIdIn(List<Long> id);

    List<ChildrenInfo> findAllByParentIdIn(List<Long> id);
}
