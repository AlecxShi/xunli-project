package com.xunli.manager.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.*;

import javax.persistence.*;
import javax.persistence.Id;
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="usertype", referencedColumnName="id")
    private DictInfo usertype;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "excit")
    private Integer excit;

    @Column(name = "create_date")
    @CreatedDate
    private Date createdate = new Date();

    @Column(name = "last_modified")
    @LastModifiedDate
    private Date lastmodified = new Date();

    @Column(name = "location")
    private String location;


    public String toString()
    {
        return String.format("{id = %s,userName = %s,passWord = %s,userType = %s,location = %s,excit = %s,createDate = %s,lastModified = %s}",
                id,username,usertype.getDictDesc(),location,excit,createdate,lastmodified);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DictInfo getUsertype() {
        return usertype;
    }

    public void setUsertype(DictInfo usertype) {
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
}
