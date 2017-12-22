package com.xunli.manager.model;

import com.xunli.manager.annotation.ColumnComment;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by shihj on 2017/12/22.
 */
@Entity
@Table(name = "app_version_info")
public class AppVersionInfo implements Serializable {

    private static final  long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ColumnComment("id")
    private Long id;

    @Column(name = "version")
    @ColumnComment("版本号")
    private String version;

    @Column(name = "desc")
    @ColumnComment("版本描述信息")
    private String versionDesc;

    @Column(name = "url")
    @ColumnComment("下载地址")
    private String url;

    @Column(name = "if_use")
    @ColumnComment("是否启用")
    private String ifUse;

    @Column(name = "create_date")
    @ColumnComment("创建日期")
    private Date createDate;

    @Column(name = "create_by")
    @ColumnComment("创建人")
    private String createBy;

    @Column(name = "last_modified")
    @ColumnComment("最后修改时间")
    private Date lastModified;

    @Column(name = "last_modified_by")
    @ColumnComment("最后修改人")
    private String lastModifiedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIfUse() {
        return ifUse;
    }

    public void setIfUse(String ifUse) {
        this.ifUse = ifUse;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
}
