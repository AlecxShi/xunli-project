package com.xunli.manager.repository;

import com.xunli.manager.model.DictInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by shihj on 2017/7/24.
 */
@Repository
public interface DictInfoRepository extends JpaRepository<DictInfo, Long>, JpaSpecificationExecutor<DictInfo> {

    List<DictInfo> findAllByDictType(String dictType);
}
