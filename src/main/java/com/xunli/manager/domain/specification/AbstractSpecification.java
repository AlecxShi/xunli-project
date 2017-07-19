package com.xunli.manager.domain.specification;

import org.springframework.data.jpa.domain.Specification;

import static org.apache.commons.lang3.StringUtils.join;
import static org.apache.commons.lang3.StringUtils.lowerCase;

public abstract class AbstractSpecification<T> implements Specification<T> {

  public static final String WILDCARD = "%";

  public String wildcards(String str) {
    return join(WILDCARD, str, WILDCARD);
  }

  public String wildcardsAndLower(String str) {
    return wildcards(lowerCase(str));
  }
  
  public String wildcardsAfter(String str){
	  return join(str,WILDCARD);
  }

}
