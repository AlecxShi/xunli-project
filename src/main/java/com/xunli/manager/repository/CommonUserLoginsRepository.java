package com.xunli.manager.repository;

import com.xunli.manager.model.CommonUserLogins;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by shihj on 2017/8/29.
 */
public interface CommonUserLoginsRepository extends JpaRepository<CommonUserLogins,Long>,JpaSpecificationExecutor<CommonUserLogins>{

    CommonUserLogins getByToken(String token);
}
