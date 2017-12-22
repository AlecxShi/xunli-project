package com.xunli.manager.repository;

import com.xunli.manager.model.AppVersionInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by shihj on 2017/12/22.
 */
public interface AppVersionInfoRepository extends JpaSpecificationExecutor<AppVersionInfo>,JpaRepository<AppVersionInfo,Long> {
}
