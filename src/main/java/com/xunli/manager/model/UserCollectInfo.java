package com.xunli.manager.model;

import com.xunli.manager.annotation.ColumnComment;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户收藏表
 * Created by shihj on 2017/8/28.
 */
@Entity
@Table(name = "user_collect_info")
public class UserCollectInfo implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ColumnComment("编号")
    private Long id;

    @Column(name = "user_id")
    @ColumnComment("用户编号")
    private Long userId;

    @Column(name = "target_user_id")
    @ColumnComment("收藏的用户编号")
    private Long targetUserId;

    @Column(name = "last_modified")
    @ColumnComment("最后修改时间")
    @LastModifiedDate
    private Date lastModified = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(Long targetUserId) {
        this.targetUserId = targetUserId;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
}
