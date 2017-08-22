package com.xunli.manager.domain.specification;

import com.xunli.manager.domain.criteria.ChildrenInfoCriteria;
import com.xunli.manager.model.ChildrenInfo;
import com.xunli.manager.model.ChildrenInfo_;

import javax.persistence.criteria.*;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
    public Predicate toPredicate(Root<ChildrenInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();
        return predicate;
    }
}
