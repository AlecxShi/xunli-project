package com.xunli.manager.domain.specification;

import com.xunli.manager.domain.criteria.ChildrenBaseInfoCriteria;
import com.xunli.manager.model.ChildrenBaseInfo;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by shihj on 2017/7/25.
 */
public class ChildrenBaseInfoSpecification extends AbstractSpecification<ChildrenBaseInfo>{
    private final ChildrenBaseInfoCriteria criteria;

    public ChildrenBaseInfoSpecification(ChildrenBaseInfoCriteria cri)
    {
        this.criteria = cri;
    }

    @Override
    public Predicate toPredicate(Root<ChildrenBaseInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return null;
    }
}
