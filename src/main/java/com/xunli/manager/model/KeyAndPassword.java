package com.xunli.manager.model;

import javax.validation.constraints.NotNull;

public class KeyAndPassword {

  @NotNull
  private String key;
  @NotNull
  private String password;

  public KeyAndPassword() {
    super();

  }

  public KeyAndPassword(String key, String password) {
    super();
    this.key = key;
    this.password = password;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "KeyAndPassword [key=" + key + ", password=" + password + "]";
  }

}
