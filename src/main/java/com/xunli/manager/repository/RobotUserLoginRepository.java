package com.xunli.manager.repository;

import com.xunli.manager.model.RobotUserLogins;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by shihj on 2017/11/9.
 */
@Repository
public interface RobotUserLoginRepository extends JpaRepository<RobotUserLogins,Long>,JpaSpecificationExecutor<RobotUserLogins>{

}
