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
@Table(name="children_extend_info")
public class ChildrenExtendInfo implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "childrenId")
    private Long childrenId;

    @Column(name="profession")
    private String profession;

    @Column(name="company")
    private String company;

    @Column(name="position")
    private String position;

    @Column(name="school")
    private String school;

    @JoinColumn(name="income",referencedColumnName = "id")
    @OneToOne
    private DictInfo income;

    @Column(name="car")
    private Boolean car;

    @JoinColumn(name="house",referencedColumnName = "id")
    @OneToOne
    private DictInfo house;

    @Column(name="photo")
    private String photo;

    @Column(name="hobby")
    private String hobby;

    @Column(name = "create_date")
    @CreatedDate
    private Date createdate = new Date();

    @Column(name = "last_modified")
    @LastModifiedDate
    private Date lastmodified = new Date();

    @Column(name = "score")
    private Integer score;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChildrenId() {
        return childrenId;
    }

    public void setChildrenId(Long childrenId) {
        this.childrenId = childrenId;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public DictInfo getIncome() {
        return income;
    }

    public void setIncome(DictInfo income) {
        this.income = income;
    }

    public Boolean getCar() {
        return car;
    }

    public void setCar(Boolean car) {
        this.car = car;
    }

    public DictInfo getHouse() {
        return house;
    }

    public void setHouse(DictInfo house) {
        this.house = house;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
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

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
