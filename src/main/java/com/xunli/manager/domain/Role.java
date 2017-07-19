package com.xunli.manager.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.xunli.manager.annotation.ColumnComment;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * 角色
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "roles")
public class Role extends AbstractAuditingEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Size(min = 0, max = 50)
    @Column(name="role_code", length = 50, nullable = false)
    @ColumnComment("角色代码")
    private String roleCode;

    @Size(min = 0, max = 50)
    @Column(name="role_name", length = 50, nullable = false)
    @ColumnComment("角色名称")
    private String roleName;

    @Size(min = 0, max = 200)
    @ColumnComment("角色描述")
    @Column(name="role_desc", length = 200)
    private String roleDesc;

    @Column(name="is_sys", nullable = false)
    @ColumnComment("是否是系统管理员")
    @JsonIgnore
    private boolean isSys = false;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<User> users = new HashSet<User>(0);

    @ManyToMany(fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "roles_authorities",
            joinColumns = @JoinColumn(name="role_id", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name="authority_id", referencedColumnName="id"))
    private Set<Authority> authorities = new HashSet<Authority>(0);

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public boolean isSys() {
        return isSys;
    }

    public void setIsSys(boolean isSys) {
        this.isSys = isSys;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }
}
