package com.xunli.manager.model;

import com.xunli.manager.annotation.ColumnComment;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by shihj on 2017/8/29.
 */
@Entity
@Table(name = "common_user_logins")
public class CommonUserLogins implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ColumnComment("编号")
    private Long id;

    @Column(name = "user_id")
    @ColumnComment("用户编号")
    private Long userId;

    @Column(name = "token")
    @ColumnComment("token")
    private String token;

    @Column(name = "ip_address")
    @ColumnComment("IP地址")
    private String ipAddress;

    @Column(name = "user_agent")
    @ColumnComment("用户agent")
    private String userAgent;

    @Column(name = "last_used")
    @ColumnComment("最后修改日期")
    private Date lastUsed;

    @Column(name = "expire_time")
    @ColumnComment("过期时间")
    private Date expireTime;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Date getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(Date lastUsed) {
        this.lastUsed = lastUsed;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }
}
