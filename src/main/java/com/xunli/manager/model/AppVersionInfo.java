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

    @Column(name = "c_version")
    @ColumnComment("版本号")
    private String currentVersion;

    @Column(name = "v_desc")
    @ColumnComment("版本描述信息")
    private String versionDesc;

    @Column(name = "file_name")
    @ColumnComment("对应文件名")
    private String fileName;

    @Column(name = "url")
    @ColumnComment("下载地址")
    private String url;

    @Column(name = "if_use")
    @ColumnComment("是否启用")
    private String ifUse;

    @Column(name = "update_level")
    @ColumnComment("更新级别")
    private Integer updateLevel;

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

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getUpdateLevel() {
        return updateLevel;
    }

    public void setUpdateLevel(Integer updateLevel) {
        this.updateLevel = updateLevel;
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
