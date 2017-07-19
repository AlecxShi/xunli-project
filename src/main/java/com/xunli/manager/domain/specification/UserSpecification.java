package com.xunli.manager.domain.specification;

import com.xunli.manager.domain.User;
import com.xunli.manager.domain.User_;
import com.xunli.manager.domain.criteria.UserCriteria;

import javax.persistence.criteria.*;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class UserSpecification extends AbstractSpecification<User>
{

	private final UserCriteria criteria;

	public UserSpecification(UserCriteria criteria)
	{
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb)
	{
		Predicate predicate = cb.conjunction();
		List<Expression<Boolean>> expressions = predicate.getExpressions();

		if (isNotBlank(criteria.getFilter()))
		{
			expressions.add(cb.or(cb.like(cb.lower(root.<String> get(User_.username)), wildcardsAndLower(criteria.getFilter())), cb.like(cb.lower(root.<String> get(User_.nickname)), wildcardsAndLower(criteria.getFilter())), cb.like(cb.lower(root.<String> get(User_.name)), wildcardsAndLower(criteria.getFilter())),
					cb.like(cb.lower(root.<String> get(User_.email)), wildcardsAndLower(criteria.getFilter())), cb.like(cb.lower(root.<String> get(User_.phone)), wildcardsAndLower(criteria.getFilter()))));
		}

		if (isNotBlank(criteria.getUsername()))
		{
			expressions.add(cb.like(cb.lower(root.<String> get(User_.username)), wildcardsAndLower(criteria.getUsername())));
		}
		if (isNotBlank(criteria.getNickname()))
		{
			expressions.add(cb.like(cb.lower(root.<String> get(User_.nickname)), wildcardsAndLower(criteria.getNickname())));
		}
		if (isNotBlank(criteria.getName()))
		{
			expressions.add(cb.like(cb.lower(root.<String> get(User_.name)), wildcardsAndLower(criteria.getName())));
		}
		if (isNotBlank(criteria.getEmail()))
		{
			expressions.add(cb.like(cb.lower(root.<String> get(User_.email)), wildcardsAndLower(criteria.getEmail())));
		}
		if (isNotBlank(criteria.getRole()))
		{
			//TODO      expressions.add(cb.isMember(criteria.getRole(), root.<String, Set<String>>get(User_.roles)));
		}
		if (null != criteria.getActivated())
		{
			expressions.add(cb.equal(root.<Boolean> get(User_.activated), criteria.getActivated()));
		}
		if (null != criteria.getEnabled())
		{
			expressions.add(cb.equal(root.<Boolean> get(User_.enabled), criteria.getEnabled()));
		}
		return predicate;

	}

}
