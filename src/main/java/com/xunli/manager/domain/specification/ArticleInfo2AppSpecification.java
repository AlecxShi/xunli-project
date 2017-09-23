package com.xunli.manager.domain.specification;

import com.xunli.manager.domain.User_;
import com.xunli.manager.domain.criteria.ArticleInfoCriteria;
import com.xunli.manager.model.ArticleInfo;
import com.xunli.manager.model.ArticleInfo_;
import com.xunli.manager.model.ChildrenInfo_;

import javax.persistence.criteria.*;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Created by shihj on 2017/9/23.
 */
public class ArticleInfo2AppSpecification extends AbstractSpecification<ArticleInfo> {

    private final ArticleInfoCriteria criteria;
    public ArticleInfo2AppSpecification(ArticleInfoCriteria criteria)
    {
        this.criteria = criteria;
    }
    @Override
    public Predicate toPredicate(Root<ArticleInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();
        List<Expression<Boolean>> expressions = predicate.getExpressions();
        expressions.add(cb.equal(root.get(ArticleInfo_.ifPublish),"Y"));
        return predicate;
    }
}
