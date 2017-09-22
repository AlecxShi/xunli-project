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
    private Long articleId;

    @Column(name = "title")
    @ColumnComment("文章标题")
    private String title;

    @Column(name = "icon_path")
    @ColumnComment("图标路径")
    private String image;

    @Column(name = "brief")
    @ColumnComment("正文内容")
    private String content;

    @Column(name = "content")
    @ColumnComment("正文内容")
    private String contentUrl;

    @Column(name = "if_publish")
    @ColumnComment("是否发布")
    private String ifPublish = "N";

    @Column(name = "read_count")
    @ColumnComment("阅读数")
    private Integer readCount = 0;

    @Column(name = "share_count")
    @ColumnComment("分享数")
    private Integer shareCount = 0;

    @Column(name = "create_user")
    @ColumnComment("创建人")
    private String createUser;

    @Column(name = "last_modified")
    @ColumnComment("最后修改日期")
    private Date lastModified = new Date();

    @Transient
    private String iconName;

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }
}
