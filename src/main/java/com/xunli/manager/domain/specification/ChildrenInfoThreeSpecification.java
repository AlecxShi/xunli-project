package com.xunli.manager.domain.specification;

import com.xunli.manager.domain.criteria.ChildrenInfoCriteria;
import com.xunli.manager.model.*;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Created by shihj on 2017/8/2.
 */
public class ChildrenInfoThreeSpecification extends AbstractSpecification<ChildrenInfo> {

    private final ChildrenInfoCriteria criteria;
    public ChildrenInfoThreeSpecification(ChildrenInfoCriteria criteria)
    {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<ChildrenInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();
        List<Expression<Boolean>> expressions = predicate.getExpressions();
        if(criteria.getGender() != null)
        {
            expressions.add(cb.equal(root.get(ChildrenInfo_.gender),criteria.getGender().getId()));
        }

        if(isNotBlank(criteria.getBornLocation()))
        {
            expressions.add(cb.equal(root.get(ChildrenInfo_.bornLocation),criteria.getBornLocation()));
        }

        if(isNotBlank(criteria.getCurrentLocation()))
        {
            expressions.add(cb.equal(root.get(ChildrenInfo_.currentLocation),criteria.getCurrentLocation().substring(0,criteria.getCurrentLocation().lastIndexOf("-"))));
        }

        criteriaQuery.orderBy(cb.desc(root.get(ChildrenInfo_.score)));

        return predicate;
    }
}
