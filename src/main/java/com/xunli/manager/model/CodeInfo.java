package com.xunli.manager.model;

import com.xunli.manager.annotation.ColumnComment;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 验证码记录表
 * Created by shihj on 2017/8/28.
 */
@Entity
@Table(name = "code_info")
public class CodeInfo implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ColumnComment("编号")
    private Long id;

    @Column(name = "user_id")
    @ColumnComment("用户编号")
    private Long userId;

    @Column(name = "code")
    @ColumnComment("发送的验证码")
    private String code;

    @Column(name = "code_expire")
    @ColumnComment("编号")
    private String codeExpire;

    @Column(name = "last_modified")
    @ColumnComment("编号")
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeExpire() {
        return codeExpire;
    }

    public void setCodeExpire(String codeExpire) {
        this.codeExpire = codeExpire;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
}
