package com.xunli.manager.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.xunli.manager.domain.Authority;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class AuthorityModel {

  private Long id;

  @NotNull
  @Size(min = 0, max = 50)
  private String authorityName;

  @Size(min = 0, max = 200)
  private String authorityDesc;

  @NotNull
  @Size(min = 0, max = 100)
  private String authorityCode;

  @Size(min = 0, max = 200)
  private String authorityPath;

  @NotNull
  private Integer authorityType;

  @NotNull
  private Integer dataLevel;

  private Authority parent;

  public AuthorityModel() {

  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public Authority getParent() {
    return parent;
  }

  public void setParent(Authority parent) {
    this.parent = parent;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
