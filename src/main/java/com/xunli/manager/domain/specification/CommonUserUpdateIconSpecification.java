package com.xunli.manager.domain.specification;

import com.xunli.manager.model.CommonUser;
import com.xunli.manager.model.CommonUser_;
import com.xunli.manager.util.DictInfoUtil;

import javax.persistence.criteria.*;
import java.util.List;

/**
 * Created by shihj on 2017/7/25.
 */
public class CommonUserUpdateIconSpecification extends AbstractSpecification<CommonUser> {

    @Override
    public Predicate toPredicate(Root<CommonUser> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();
        List<Expression<Boolean>> expressions = predicate.getExpressions();
        expressions.add(cb.and(cb.isNull(root.get(CommonUser_.icon)),
                cb.equal(root.get(CommonUser_.usertype), DictInfoUtil.getRobotUserType())));
        criteriaQuery.orderBy(cb.asc(root.get(CommonUser_.id)));
        return predicate;
    }
}
