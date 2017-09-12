package com.xunli.manager.model;

import com.xunli.manager.annotation.ColumnComment;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by shihj on 2017/9/11.
 */
@Entity
@Table(name = "article_info")
public class ArticleInfo implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ColumnComment("id")
    private Long id;

    @Column(name = "title")
    @ColumnComment("文章标题")
    private String title;

    @Column(name = "icon_path")
    @ColumnComment("图标路径")
    private String iconPath;

    @Column(name = "content")
    @ColumnComment("正文内容")
    private String content;

    @Column(name = "if_publish")
    @ColumnComment("是否发布")
    private String ifPublish;

    @Column(name = "read_count")
    @ColumnComment("阅读数")
    private Integer readCount;

    @Column(name = "share_count")
    @ColumnComment("分享数")
    private Integer shareCount;

    @Column(name = "create_user")
    @ColumnComment("创建人")
    private String createUser;

    @Column(name = "last_modified")
    @ColumnComment("最后修改日期")
    private Date lastModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIfPublish() {
        return ifPublish;
    }

    public void setIfPublish(String ifPublish) {
        this.ifPublish = ifPublish;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public Integer getShareCount() {
        return shareCount;
    }

    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
}
