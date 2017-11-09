package com.xunli.manager.domain.specification;

import com.xunli.manager.domain.criteria.RobotUserLoginCriteria;
import com.xunli.manager.model.RobotUserLogins;
import com.xunli.manager.model.RobotUserLogins_;

import javax.persistence.criteria.*;
import java.util.List;

/**
 * Created by shihj on 2017/11/9.
 */
public class RobotUserLoginSpecification extends AbstractSpecification<RobotUserLogins>{
    private final RobotUserLoginCriteria criteria;

    public RobotUserLoginSpecification(RobotUserLoginCriteria robotUserLoginCriteria)
    {
        this.criteria = robotUserLoginCriteria;
    }
    @Override
    public Predicate toPredicate(Root<RobotUserLogins> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();
        List<Expression<Boolean>> expressions = predicate.getExpressions();
        if(criteria.getStartTime() != null && criteria.getEndTime() != null)
        {
            expressions.add(cb.between(root.get(RobotUserLogins_.lastModified),criteria.getStartTime(),criteria.getEndTime()));
        }
        criteriaQuery.orderBy(cb.desc(root.get(RobotUserLogins_.lastModified)),cb.desc(root.get(RobotUserLogins_.msgCount)));
        return predicate;
    }
}
