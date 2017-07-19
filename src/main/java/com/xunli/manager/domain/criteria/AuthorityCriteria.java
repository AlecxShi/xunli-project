package com.xunli.manager.domain.criteria;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorityCriteria {

  private String filter;

  private String authorityName;

  private String authorityDesc;

  private String authorityCode;

  private String authorityPath;

  private Integer authorityType;

  private Integer dataLevel;

  private Boolean isSys;

  private Long parentId;

  public String getFilter() {
    return filter;
  }

  public void setFilter(String filter) {
    this.filter = filter;
  }

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

  public Boolean getIsSys() {
    return isSys;
  }

  public void setIsSys(Boolean isSys) {
    this.isSys = isSys;
  }

  public Long getParentId() {
    return parentId;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }

}
