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
public class RecommendInfo implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NotNull
    @ColumnComment("编号")
    private Long id;

    @JoinColumn(name = "children_id",referencedColumnName = "id")
    @OneToOne
    @ColumnComment("用户子女编号")
    private ChildrenInfo childrenId;

    @JoinColumn(name = "target_children_id",referencedColumnName = "id")
    @OneToOne
    @ColumnComment("匹配的用户子女编号")
    private ChildrenInfo targetChildrenId;

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

    public ChildrenInfo getChildrenId() {
        return childrenId;
    }

    public void setChildrenId(ChildrenInfo childrenId) {
        this.childrenId = childrenId;
    }

    public ChildrenInfo getTargetChildrenId() {
        return targetChildrenId;
    }

    public void setTargetChildrenId(ChildrenInfo targetChildrenId) {
        this.targetChildrenId = targetChildrenId;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
}
