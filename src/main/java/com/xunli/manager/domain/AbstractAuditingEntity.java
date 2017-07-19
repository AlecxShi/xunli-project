package com.xunli.manager.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.xunli.manager.annotation.ColumnComment;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

@SuppressWarnings("serial")
@JsonInclude(Include.NON_NULL)
@MappedSuperclass
@Audited
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditingEntity implements Serializable {

  //获得需要记录的信息
  public String toRecord(){
    StringBuffer sbf = new StringBuffer();
    sbf.append("id,createdDate,createdBy,lastModifiedBy,lastModifiedDate");
    Field[] fields = this.getClass().getDeclaredFields();
    for(Field f : fields){
      sbf.append(",").append(f.getName());
    }
    return toStr(sbf.toString());
  }

  public String toStr(String names){
    StringBuffer sbf = new StringBuffer("{");
    try{
      for(String name : names.split(",")){
        if("id".equals(name)){
          sbf.append("\"id\":\"" + this.getId() + "\",");
        }else if("createdDate".equals(name)){
          sbf.append("\"创建时间\":\"" + this.getCreatedDate() + "\",");
        }else if("createdBy".equals(name)){
          sbf.append("\"创建人\":\"" + this.getCreatedBy() + "\",");
        }else if("lastModifiedDate".equals(name)){
          sbf.append("\"最后修改时间\":\"" + this.getLastModifiedDate() + "\",");
        }else if("lastModifiedBy".equals(name)){
          sbf.append("\"最后修改人\":\"" + this.getLastModifiedBy() + "\",");
        }else{
          Field f = this.getClass().getDeclaredField(name);
          f.setAccessible(true);
          ColumnComment cc = f.getAnnotation(ColumnComment.class);
          String str = f.getName();
          if(cc != null && cc.value() != null){
            str = cc.value();
          }
          sbf.append("\"" + str + "\":\"" + f.get(this) + "\",");
        }

      }
      sbf.deleteCharAt(sbf.length() - 1);
      sbf.append("}");
    }catch(Exception e){

    }
    return sbf.toString();
  }
  
  @Id 
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  @ColumnComment("id")
  private Long id;
  
  @CreatedBy
  @Column(name = "created_by", nullable = false, length = 50, updatable = false)
  @JsonIgnore
  @ColumnComment("创建人")
  private String createdBy;

  @CreatedDate
  @Column(name = "created_date", nullable = false, updatable = false)
  @JsonIgnore
  @ColumnComment("创建时间")
  private LocalDateTime createdDate = LocalDateTime.now();

  @LastModifiedBy
  @Column(name = "last_modified_by", length = 50)
  @JsonIgnore
  @ColumnComment("最后修改人")
  private String lastModifiedBy;

  @LastModifiedDate
  @Column(name = "last_modified_date")
  @JsonIgnore
  @ColumnComment("最后修改时间")
  private LocalDateTime lastModifiedDate = LocalDateTime.now();



  public void setId(Long id) {
	  this.id = id;
  }

  public Long getId() {
		return id;
	}
  
  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }

  public String getLastModifiedBy() {
    return lastModifiedBy;
  }

  public void setLastModifiedBy(String lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
  }

  public LocalDateTime getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }


}
