package com.xunli.manager.domain.specification;

import com.xunli.manager.domain.criteria.ChildrenInfoCriteria;
import com.xunli.manager.model.ChildrenInfo;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by shihj on 2017/8/2.
 */
public class ChildrenInfoSpecification extends AbstractSpecification<ChildrenInfo> {

    private final ChildrenInfoCriteria criteria;
    public ChildrenInfoSpecification(ChildrenInfoCriteria criteria)
    {
        this.criteria = criteria;
    }
    @Override
    public Predicate toPredicate(Root<ChildrenInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return null;
    }
}
