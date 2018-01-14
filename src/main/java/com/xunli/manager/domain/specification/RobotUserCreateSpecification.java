package com.xunli.manager.domain.specification;

import com.xunli.manager.domain.criteria.RobotUserCheckConditionCriteria;
import com.xunli.manager.model.ChildrenInfo;
import com.xunli.manager.model.ChildrenInfoTwo_;
import com.xunli.manager.model.ChildrenInfo_;
import com.xunli.manager.util.DateUtil;
import com.xunli.manager.util.DictInfoUtil;
import org.thymeleaf.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Created by Betty on 2018/1/14.
 */
public class RobotUserCreateSpecification extends AbstractSpecification<ChildrenInfo>{

    private final RobotUserCheckConditionCriteria criteria;

    public RobotUserCreateSpecification(RobotUserCheckConditionCriteria criteria)
    {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<ChildrenInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();
        List<Expression<Boolean>> expressions = predicate.getExpressions();
        if(!StringUtils.isEmpty(criteria.getBornLocation()))
        {
            expressions.add(cb.equal(root.get(ChildrenInfo_.bornLocation),criteria.getBornLocation()));
        }

        if(!StringUtils.isEmpty(criteria.getCurrentLocation()))
        {
            expressions.add(cb.equal(root.get(ChildrenInfo_.currentLocation),criteria.getCurrentLocation()));
        }

        if(criteria.getGender() != null)
        {
            expressions.add(cb.equal(root.get(ChildrenInfo_.gender), DictInfoUtil.getOppositeSex(DictInfoUtil.getItemById(criteria.getGender())).getId()));
        }
        if(!"1".equals(criteria.getOpType()))
        {
            if(isNotBlank(criteria.getBirthday()))
            {
                if("Male".equals(DictInfoUtil.getItemById(criteria.getGender()).getDictValue()))
                {
                    expressions.add(cb.between(root.get(ChildrenInfo_.birthday),DateUtil.getDate(criteria.getBirthday(),- 15),DateUtil.getDate(criteria.getBirthday(),8)));
                }
                else
                {
                    expressions.add(cb.between(root.get(ChildrenInfo_.birthday),DateUtil.getDate(criteria.getBirthday(),-8),DateUtil.getDate(criteria.getBirthday(),15)));
                }

            }
        }
        return predicate;
    }
}
