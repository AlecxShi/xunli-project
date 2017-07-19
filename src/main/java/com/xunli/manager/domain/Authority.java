package com.xunli.manager.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.xunli.manager.annotation.ColumnComment;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "authorities")
public class Authority extends AbstractAuditingEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = 6076093606047147130L;

	@Size(min = 0, max = 50)
    @Column(name="authority_name", length = 50, nullable = false)
    @ColumnComment("权限名称")
    private String authorityName;

    @Size(min = 0, max = 200)
    @Column(name="authority_desc", length = 200)
    @ColumnComment("权限描述")
    private String authorityDesc;

    @Size(min = 0, max = 100)
    @Column(name="authority_code", length = 100, nullable = false)
    @ColumnComment("权限代码")
    private String authorityCode;

    @Size(min = 0, max = 200)
    @Column(name="authority_path", length = 200)
    @ColumnComment("权限路径")
    private String authorityPath;

    @Column(name="authority_type", nullable = false)
    @ColumnComment("权限类型")
    private Integer authorityType = 2;//权限类型，0．表示目录　1，表示菜单．2，表示按扭. 3，表示功能

    @Column(name="data_level", nullable = false)//优先级，排序
    @ColumnComment("优先级")
    private Integer dataLevel = 1;

    @JsonIgnore
    @Column(name="is_sys", nullable = false)
    @ColumnComment("是否有系统权限")
    private boolean isSys = false;

    @JsonIgnore
    @ManyToMany(mappedBy = "authorities")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Role> roles = new HashSet<Role>(0);

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    private Authority parent;

    @JsonIgnore
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Collection<Authority> children;

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    public String getAuthorityDesc() {
        return authorityDesc;
    }

    public void setAuthorityDesc(String authorityDesc) {
        this.authorityDesc = authorityDesc;
    }

    public String getAuthorityCode() {
        return authorityCode;
    }

    public void setAuthorityCode(String authorityCode) {
        this.authorityCode = authorityCode;
    }

    public String getAuthorityPath() {
        return authorityPath;
    }

    public void setAuthorityPath(String authorityPath) {
        this.authorityPath = authorityPath;
    }

    public Integer getAuthorityType() {
        return authorityType;
    }

    public void setAuthorityType(Integer authorityType) {
        this.authorityType = authorityType;
    }

    public Integer getDataLevel() {
        return dataLevel;
    }

    public void setDataLevel(Integer dataLevel) {
        this.dataLevel = dataLevel;
    }

    public boolean isSys() {
        return isSys;
    }

    public void setIsSys(boolean isSys) {
        this.isSys = isSys;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Authority getParent() {
        return parent;
    }

    public void setParent(Authority parent) {
        this.parent = parent;
    }

    public Collection<Authority> getChildren() {
        return children;
    }

    public void setChildren(Collection<Authority> children) {
        this.children = children;
    }
}
