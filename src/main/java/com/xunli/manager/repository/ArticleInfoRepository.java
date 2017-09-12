package com.xunli.manager.repository;

import com.xunli.manager.model.ArticleInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by shihj on 2017/9/11.
 */
public interface ArticleInfoRepository extends JpaSpecificationExecutor<ArticleInfo>,JpaRepository<ArticleInfo,Long>{

}
