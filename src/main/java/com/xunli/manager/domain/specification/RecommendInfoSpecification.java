package com.xunli.manager.domain.specification;

import com.xunli.manager.domain.criteria.RecommendInfoCriteria;
import com.xunli.manager.model.*;

import javax.persistence.criteria.*;
import java.util.List;

/**
 * Created by shihj on 2017/8/8.
 */
public class RecommendInfoSpecification extends AbstractSpecification<RecommendInfo>{
    private final RecommendInfoCriteria criteria;

    public RecommendInfoSpecification(RecommendInfoCriteria criteria)
    {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<RecommendInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();
        List<Expression<Boolean>> expressions = predicate.getExpressions();
        if(criteria.getChildrenId() != null)
        {
            expressions.add(cb.equal(root.get(RecommendInfo_.childrenId).get(ChildrenInfo_.id),criteria.getChildrenId()));
        }
        return predicate;
    }
}
