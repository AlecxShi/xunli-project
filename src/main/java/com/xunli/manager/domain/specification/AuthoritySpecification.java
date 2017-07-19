package com.xunli.manager.domain.specification;


import com.xunli.manager.domain.Authority;
import com.xunli.manager.domain.Authority_;
import com.xunli.manager.domain.criteria.AuthorityCriteria;

import javax.persistence.criteria.*;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class AuthoritySpecification extends AbstractSpecification<Authority> {

  private final AuthorityCriteria criteria;

  public AuthoritySpecification(AuthorityCriteria criteria) {
    this.criteria = criteria;
  }

  @Override
  public Predicate toPredicate(Root<Authority> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
    Predicate predicate = cb.conjunction();
    List<Expression<Boolean>> expressions = predicate.getExpressions();

    if (isNotBlank(criteria.getFilter())) {
      expressions.add(cb.or(
              cb.like(cb.lower(root.<String>get(Authority_.authorityName)),
                      wildcardsAndLower(criteria.getFilter())),
              cb.like(cb.lower(root.<String>get(Authority_.authorityDesc)),
                      wildcardsAndLower(criteria.getFilter())),
              cb.like(cb.lower(root.<String>get(Authority_.authorityCode)),
                      wildcardsAndLower(criteria.getFilter())),
              cb.like(cb.lower(root.<String>get(Authority_.authorityPath)),
                      wildcardsAndLower(criteria.getFilter()))
      ));
    }

    if (isNotBlank(criteria.getAuthorityName())) {
      expressions.add(cb.like(cb.lower(root.<String>get(Authority_.authorityName)),
              wildcardsAndLower(criteria.getAuthorityName())));
    }
    if (isNotBlank(criteria.getAuthorityCode())) {
      expressions.add(cb.like(cb.lower(root.<String>get(Authority_.authorityCode)),
          wildcardsAndLower(criteria.getAuthorityCode())));
    }
      if (isNotBlank(criteria.getAuthorityDesc())) {
          expressions.add(cb.like(cb.lower(root.<String>get(Authority_.authorityDesc)),
                  wildcardsAndLower(criteria.getAuthorityDesc())));
      }
      if (isNotBlank(criteria.getAuthorityPath())) {
          expressions.add(cb.like(cb.lower(root.<String>get(Authority_.authorityPath)),
                  wildcardsAndLower(criteria.getAuthorityPath())));
      }
      if (null != criteria.getAuthorityType()) {
          expressions.add(cb.equal(root.<Integer>get(Authority_.authorityType), criteria.getAuthorityType()));
      }
      if (null != criteria.getDataLevel()) {
          expressions.add(cb.equal(root.<Integer>get(Authority_.dataLevel), criteria.getDataLevel()));
      }
//      if (null != criteria.getParentId()) {
//          expressions.add(cb.equal(root.<Authority>get(Authority_.parent), criteria.getDataLevel()));
//      }
    if (null != criteria.getIsSys()) {
      expressions.add(cb.equal(root.<Boolean>get(Authority_.isSys), criteria.getIsSys()));
    }
    return predicate;

  }

}
