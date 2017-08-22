package com.xunli.manager.model;

import com.xunli.manager.annotation.ColumnComment;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by shihj on 2017/8/8.
 */
@Entity
@Table(name = "recommend_info")
public class RecommendInfoTwo implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ColumnComment("编号")
    private Long id;

    @Column(name = "children_id")
    @ColumnComment("用户子女编号")
    private Long childrenId;

    @Column(name = "target_children_id")
    @ColumnComment("匹配的用户子女编号")
    private Long targetChildrenId;

    @Column(name = "last_modified")
    @LastModifiedDate
    @ColumnComment("最后修改日期")
    private Date lastModified = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChildrenId() {
        return childrenId;
    }

    public void setChildrenId(Long childrenId) {
        this.childrenId = childrenId;
    }

    public Long getTargetChildrenId() {
        return targetChildrenId;
    }

    public void setTargetChildrenId(Long targetChildrenId) {
        this.targetChildrenId = targetChildrenId;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
}
