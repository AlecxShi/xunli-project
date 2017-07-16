package com.xunli.manager.repository;

import com.xunli.manager.model.CommonUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Betty on 2017/7/16.
 */
public interface CommonUserRepository extends JpaRepository<CommonUser, Long>, JpaSpecificationExecutor<CommonUser> {

}
