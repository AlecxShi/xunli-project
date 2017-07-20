package com.xunli.manager.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@JsonInclude(Include.NON_NULL)
public class ValidationResult {

  public static final ValidationResult VALID = new ValidationResult(true);

  public static final ValidationResult INVALID = new ValidationResult(false);

  private boolean valid;

  private Object value;

  public ValidationResult() {
    super();

  }

  public ValidationResult(boolean valid) {
    super();
    this.valid = valid;

  }

  public ValidationResult(boolean valid, Object value) {
    super();
    this.valid = valid;
    this.value = value;
  }

  public boolean isValid() {
    return valid;
  }

  public void setValid(boolean valid) {
    this.valid = valid;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }

}
