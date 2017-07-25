package com.xunli.manager.domain.specification;

import com.xunli.manager.domain.criteria.ChildrenExtendInfoCriteria;
import com.xunli.manager.model.ChildrenExtendInfo;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by shihj on 2017/7/25.
 */
public class ChildrenExtendInfoSpecification extends AbstractSpecification<ChildrenExtendInfo>{
    private final ChildrenExtendInfoCriteria criteria;

    public ChildrenExtendInfoSpecification(ChildrenExtendInfoCriteria cri)
    {
        this.criteria = cri;
    }
    @Override
    public Predicate toPredicate(Root<ChildrenExtendInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return null;
    }
}
