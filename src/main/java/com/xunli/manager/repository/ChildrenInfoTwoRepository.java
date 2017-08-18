package com.xunli.manager.repository;

import com.xunli.manager.model.ChildrenInfoTwo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by shihj on 2017/8/11.
 */
@Repository
public interface ChildrenInfoTwoRepository extends JpaRepository<ChildrenInfoTwo,Long>,JpaSpecificationExecutor<ChildrenInfoTwo> {

}
