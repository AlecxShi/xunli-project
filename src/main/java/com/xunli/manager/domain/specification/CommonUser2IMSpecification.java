package com.xunli.manager.domain.specification;

import com.xunli.manager.model.CommonUser;
import com.xunli.manager.model.CommonUser_;

import javax.persistence.criteria.*;
import java.util.List;

/**
 * Created by shihj on 2017/7/25.
 */
public class CommonUser2IMSpecification extends AbstractSpecification<CommonUser> {

    @Override
    public Predicate toPredicate(Root<CommonUser> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();
        List<Expression<Boolean>> expressions = predicate.getExpressions();
        expressions.add(cb.notEqual(root.get(CommonUser_.ifRegister),"Y"));
        return predicate;
    }
}
