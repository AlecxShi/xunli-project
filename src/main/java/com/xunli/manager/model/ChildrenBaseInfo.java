package com.xunli.manager.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by shihj on 2017/7/25.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name="children_base_info")
public class ChildrenBaseInfo implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "parentId")
    private Long parentId;

    @JoinColumn(name="gender",referencedColumnName = "id")
    @OneToOne
    private DictInfo gender;

    @Column(name="bornLocation")
    private String bornLocation;

    @Column(name="currentLocation")
    private String currentLocation;

    @Column(name="age")
    private Integer age;

    @Column(name = "birthday")
    private String birthday;

    @Column(name="height")
    private Integer height;

    @JoinColumn(name="education",referencedColumnName = "id")
    @OneToOne
    private DictInfo education;

    @Column(name="score")
    private Integer score;

    @Column(name = "create_date")
    @CreatedDate
    private Date createdate = new Date();

    @Column(name = "last_modified")
    @LastModifiedDate
    private Date lastmodified = new Date();

    @Column(name = "label")
    private String label;

    @Column(name = "name")
    private String name;

    @Transient
    private ChildrenExtendInfo extendInfo;

    @Transient
    private CommonUser parent;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public DictInfo getGender() {
        return gender;
    }

    public void setGender(DictInfo gender) {
        this.gender = gender;
    }

    public String getBornLocation() {
        return bornLocation;
    }

    public void setBornLocation(String bornLocation) {
        this.bornLocation = bornLocation;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public DictInfo getEducation() {
        return education;
    }

    public void setEducation(DictInfo education) {
        this.education = education;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
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

    public ChildrenExtendInfo getExtendInfo() {
        return extendInfo;
    }

    public void setExtendInfo(ChildrenExtendInfo extendInfo) {
        this.extendInfo = extendInfo;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParent(CommonUser parent) {
        this.parent = parent;
    }

    public CommonUser getParent() {
        return parent;
    }
}
