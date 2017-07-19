package com.xunli.manager.domain.criteria;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xunli.manager.domain.Authority;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleCriteria {

  private String filter;

  private String roleName;

  private String roleDesc;

  private Boolean isSys;

  private Set<Authority> authorities;

  public String getFilter() {
    return filter;
  }

  public void setFilter(String filter) {
    this.filter = filter;
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

  public Boolean getIsSys() {
    return isSys;
  }

  public void setIsSys(Boolean isSys) {
    this.isSys = isSys;
  }

  public Set<Authority> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(Set<Authority> authorities) {
    this.authorities = authorities;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }

}
