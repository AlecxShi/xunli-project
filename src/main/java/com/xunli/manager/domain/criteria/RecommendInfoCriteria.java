package com.xunli.manager.domain.criteria;

/**
 * Created by shihj on 2017/8/8.
 */
public class RecommendInfoCriteria {
    private Long childrenId;

    public RecommendInfoCriteria(Long childrenId)
    {
        this.childrenId = childrenId;
    }

    public Long getChildrenId() {
        return childrenId;
    }

    public void setChildrenId(Long childrenId) {
        this.childrenId = childrenId;
    }
}
