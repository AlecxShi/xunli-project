package com.xunli.manager.domain.specification;

import com.xunli.manager.domain.criteria.DictInfoCriteria;
import com.xunli.manager.model.DictInfo;
import com.xunli.manager.model.DictInfo_;

import javax.persistence.criteria.*;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Created by shihj on 2017/7/24.
 */
public class DictInfoSpecification  extends AbstractSpecification<DictInfo>{
    private final DictInfoCriteria criteria;

    public DictInfoSpecification(DictInfoCriteria criteria)
    {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<DictInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();
        List<Expression<Boolean>> expressions = predicate.getExpressions();

        if (isNotBlank(criteria.getFilter())) {
            expressions.add(cb.or(
                    cb.like(cb.lower(root.<String>get(DictInfo_.dictType)),
                            wildcardsAndLower(criteria.getFilter())),
                    cb.like(cb.lower(root.<String>get(DictInfo_.dictValue)),
                            wildcardsAndLower(criteria.getFilter())),
                    cb.like(cb.lower(root.<String>get(DictInfo_.dictDesc)),
                            wildcardsAndLower(criteria.getFilter()))
            ));
        }
        return predicate;
    }
}
