package com.xunli.manager.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xunli.manager.annotation.ColumnComment;
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
@Table(name="children_info")
public class ChildrenInfo implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    @ColumnComment("编号")
    private Long id;

    @Column(name = "name")
    @ColumnComment("显示名称")
    private String name;

    @Column(name = "parentId")
    @ColumnComment("所属用户")
    private Long parentId;

    @JoinColumn(name="gender",referencedColumnName = "id")
    @OneToOne
    @ColumnComment("性别")
    private DictInfo gender;

    @Column(name="bornLocation")
    @ColumnComment("家乡所在地")
    private String bornLocation;

    @Column(name="currentLocation")
    @ColumnComment("当前所在地")
    private String currentLocation;

    @Column(name = "birthday")
    @ColumnComment("出生年份")
    private String birthday;

    @Column(name="height")
    @ColumnComment("身高")
    private Integer height;

    @JoinColumn(name="education",referencedColumnName = "id")
    @OneToOne
    @ColumnComment("教育程度")
    private DictInfo education;

    @Column(name="profession")
    @ColumnComment("职业信息")
    private String profession;

    @Column(name="company")
    @ColumnComment("公司信息")
    private String company;

    @Column(name="position")
    @ColumnComment("职位信息")
    private String position;

    @Column(name="school")
    @ColumnComment("毕业学校")
    private String school;

    @JoinColumn(name="income",referencedColumnName = "id")
    @OneToOne
    @ColumnComment("收入等级")
    private DictInfo income;

    @Column(name="car")
    @ColumnComment("是否有车")
    private Boolean car;

    @JoinColumn(name="house",referencedColumnName = "id")
    @OneToOne
    @ColumnComment("房产信息")
    private DictInfo house;

    @Column(name="photo")
    @ColumnComment("照片存储路径")
    private String photo;

    @Column(name="hobby")
    @ColumnComment("爱好信息")
    private String hobby;

    @Column(name="score")
    @ColumnComment("静态评分")
    private Integer score;

    @Column(name = "label")
    @ColumnComment("标签信息")
    private String label;

    @Column(name = "create_date")
    @CreatedDate
    @ColumnComment("创建日期")
    private Date createdate = new Date();

    @Column(name = "last_modified")
    @LastModifiedDate
    @ColumnComment("最后修改日期")
    private Date lastmodified = new Date();

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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setParent(CommonUser parent) {
        this.parent = parent;
    }

    public CommonUser getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
