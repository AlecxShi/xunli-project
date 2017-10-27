package com.xunli.manager.model;

import com.xunli.manager.annotation.ColumnComment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by shihj on 2017/10/26.
 * 持久化记录假用户收到的消息数量
 */
@Entity
@Table(name = "robot_user_logins")
public class RobotUserLogins implements Serializable{

    private final static long serialVersionUID = 1L;

    @Id
    @NotNull
    @Column(name = "user_id")
    @ColumnComment("假用户编号")
    private Long userId;

    @Column(name = "msg_count")
    @ColumnComment("假用户消息数量")
    @NotNull
    private Integer msgCount = 0;

    @Column(name = "his_msg_count")
    @ColumnComment("假用户历史消息数量")
    @NotNull
    private Integer hisMsgCount = 0;


    @Column(name = "last_modified")
    @ColumnComment("最后记录时间")
    @NotNull
    private Date lastModified = new Date();

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(Integer msgCount) {
        this.msgCount = msgCount;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Integer getHisMsgCount() {
        return hisMsgCount;
    }

    public void setHisMsgCount(Integer hisMsgCount) {
        this.hisMsgCount = hisMsgCount;
    }
}
