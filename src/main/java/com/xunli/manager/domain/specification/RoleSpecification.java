package com.xunli.manager.domain.specification;


import com.xunli.manager.domain.Role;
import com.xunli.manager.domain.Role_;
import com.xunli.manager.domain.criteria.RoleCriteria;

import javax.persistence.criteria.*;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class RoleSpecification extends AbstractSpecification<Role> {

  private final RoleCriteria criteria;

  public RoleSpecification(RoleCriteria criteria) {
    this.criteria = criteria;
  }

  @Override
  public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
    Predicate predicate = cb.conjunction();
    List<Expression<Boolean>> expressions = predicate.getExpressions();

    if (isNotBlank(criteria.getFilter())) {
      expressions.add(cb.or(
          cb.like(cb.lower(root.<String>get(Role_.roleName)),
              wildcardsAndLower(criteria.getFilter())),
          cb.like(cb.lower(root.<String>get(Role_.roleDesc)),
              wildcardsAndLower(criteria.getFilter()))
      ));
    }

    if (isNotBlank(criteria.getRoleName())) {
      expressions.add(cb.like(cb.lower(root.<String>get(Role_.roleName)),
          wildcardsAndLower(criteria.getRoleName())));
    }
    if (isNotBlank(criteria.getRoleDesc())) {
      expressions.add(cb.like(cb.lower(root.<String>get(Role_.roleDesc)),
          wildcardsAndLower(criteria.getRoleDesc())));
    }
    if (null != criteria.getIsSys()) {
      expressions.add(cb.equal(root.<Boolean>get(Role_.isSys), criteria.getIsSys()));
    }
    return predicate;

  }

}
