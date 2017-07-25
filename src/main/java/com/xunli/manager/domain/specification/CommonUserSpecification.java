package com.xunli.manager.domain.specification;

import com.xunli.manager.domain.criteria.CommonUserCriteria;
import com.xunli.manager.model.CommonUser;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by shihj on 2017/7/25.
 */
public class CommonUserSpecification extends AbstractSpecification<CommonUser> {
    private final CommonUserCriteria criteria;

    public CommonUserSpecification(CommonUserCriteria criteria)
    {
        this.criteria =  criteria;
    }
    @Override
    public Predicate toPredicate(Root<CommonUser> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
        return null;
    }
}
