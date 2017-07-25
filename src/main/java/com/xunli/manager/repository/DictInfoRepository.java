package com.xunli.manager.repository;

import com.xunli.manager.model.DictInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by shihj on 2017/7/24.
 */
public interface DictInfoRepository extends JpaRepository<DictInfo, Long>, JpaSpecificationExecutor<DictInfo> {

}
