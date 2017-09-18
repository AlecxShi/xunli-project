package com.xunli.manager.domain.specification;

import com.xunli.manager.domain.criteria.ArticleInfoCriteria;
import com.xunli.manager.domain.criteria.ChildrenBaseInfoCriteria;
import com.xunli.manager.model.ArticleInfo;
import com.xunli.manager.model.ChildrenInfo;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by shihj on 2017/9/18.
 */
public class ArticleInfoSpecification extends AbstractSpecification<ArticleInfo> {

    private final ArticleInfoCriteria criteria;
    public ArticleInfoSpecification(ArticleInfoCriteria criteria)
    {
        this.criteria = criteria;
    }
    @Override
    public Predicate toPredicate(Root<ArticleInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return null;
    }
}
