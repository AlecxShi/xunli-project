package com.xunli.manager.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {

  // @Unique(service = UserValidator.class, fieldName = "username",
  // message = "{username.unique.violation}")
  @NotNull
  @Pattern(regexp = "^[a-z0-9]*$")
  @Size(min = 0, max = 50)
  private String username;

  @Size(min = 0, max = 100)
  private String password;

  // @Unique(service = UserValidator.class, fieldName = "email", message =
  // "{email.unique.violation}")
  @Email
  @Size(min = 0, max = 255)
  private String email;

  public Account() {

  }

  public Account(String username, String password, String email) {
    super();
    this.username = username;
    this.password = password;
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
