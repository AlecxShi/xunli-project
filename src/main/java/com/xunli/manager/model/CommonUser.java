package com.xunli.manager.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.*;

import javax.persistence.*;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Betty on 2017/7/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "common_user_info")
public class CommonUser implements Serializable{

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="usertype", referencedColumnName="id")*/
    @Column(name="usertype")
    private Long usertype;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "location")
    private String location;

    @Column(name = "phone")
    private String phone;

    @Column(name = "excit")
    private Integer excit;

    @Column(name = "create_date")
    @CreatedDate
    private Date createdate = new Date();

    @Column(name = "last_modified")
    @LastModifiedDate
    private Date lastmodified = new Date();

    @Transient
    private ChildrenInfo children;

    @Transient
    private ChildrenInfoTwo childrenInfoTwo;

    public String toString()
    {
        return String.format("{id = %s,userName = %s,passWord = %s,userType = %s,location = %s,excit = %s,createDate = %s,lastModified = %s}",
                id,username,usertype,location,excit,createdate,lastmodified);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsertype() {
        return usertype;
    }

    public void setUsertype(Long usertype) {
        this.usertype = usertype;
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

    public Integer getExcit() {
        return excit;
    }

    public void setExcit(Integer excit) {
        this.excit = excit;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Date getLastmodified() {
        return lastmodified;
    }

    public void setLastmodified(Date lastmodified) {
        this.lastmodified = lastmodified;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ChildrenInfo getChildren() {
        return children;
    }

    public void setChildren(ChildrenInfo children) {
        this.children = children;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ChildrenInfoTwo getChildrenInfoTwo() {
        return childrenInfoTwo;
    }

    public void setChildrenInfoTwo(ChildrenInfoTwo childrenInfoTwo) {
        this.childrenInfoTwo = childrenInfoTwo;
    }
}
