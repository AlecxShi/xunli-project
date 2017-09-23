package com.xunli.manager.domain.specification;

import com.xunli.manager.domain.criteria.ChildrenInfoCriteria;
import com.xunli.manager.model.ChildrenInfoTwo;
import com.xunli.manager.model.ChildrenInfoTwo_;
import com.xunli.manager.model.CommonUser_;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Created by shihj on 2017/8/2.
 */
public class ChildrenInfoTwoSpecification extends AbstractSpecification<ChildrenInfoTwo> {

    private final ChildrenInfoCriteria criteria;
    public ChildrenInfoTwoSpecification(ChildrenInfoCriteria criteria)
    {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<ChildrenInfoTwo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();
        List<Expression<Boolean>> expressions = predicate.getExpressions();
        if(criteria.getGender() != null)
        {
            expressions.add(cb.equal(root.get(ChildrenInfoTwo_.gender),criteria.getGender().getId()));
        }

        if(isNotBlank(criteria.getBornLocation()))
        {
            expressions.add(cb.equal(root.get(ChildrenInfoTwo_.bornLocation),criteria.getBornLocation()));
        }

        if(isNotBlank(criteria.getCurrentLocation()))
        {
            expressions.add(cb.equal(root.get(ChildrenInfoTwo_.currentLocation),criteria.getCurrentLocation().substring(0,criteria.getCurrentLocation().lastIndexOf("-"))));
        }

        if(criteria.getEducation() != null && !criteria.getEducation().isEmpty())
        {
            expressions.add(root.get(ChildrenInfoTwo_.education).in(criteria.getEducation()));
        }

        if(isNotBlank(criteria.getStartBirthday()) && isNotBlank(criteria.getEndBirthday()))
        {
            expressions.add(cb.between(root.get(ChildrenInfoTwo_.birthday),criteria.getStartBirthday(),criteria.getEndBirthday()));
        }

        if(criteria.getNum().equals(3))
        {
            criteriaQuery.orderBy(cb.desc(root.get(ChildrenInfoTwo_.score)));
        }
        else
        {
            if(criteria.getExcept() != null && !criteria.getExcept().isEmpty())
            {
                List<Long> ids = new ArrayList();
                for(ChildrenInfoTwo t : criteria.getExcept())
                {
                    ids.add(t.getId());
                }
                expressions.add(cb.not(root.get(ChildrenInfoTwo_.id).in(ids)));
            }
            criteriaQuery.orderBy(cb.asc(root.get(ChildrenInfoTwo_.parent).get(CommonUser_.usertype)),cb.desc(root.get(ChildrenInfoTwo_.score)));
        }
        return predicate;
    }
}
