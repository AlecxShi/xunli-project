package com.xunli.manager.domain.criteria;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xunli.manager.annotation.ColumnComment;

import javax.persistence.Column;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by shihj on 2017/9/6.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChildrenInfoModel implements Serializable{

    private final static long serialVersionUID = 1L;

    //用户token
    @ColumnComment("用户token")
    private String token;

    @ColumnComment("显示名称")
    private String name;

    @ColumnComment("性别")
    private Long gender;

    @ColumnComment("家乡所在地")
    private String bornLocation;

    @ColumnComment("当前所在地")
    private String currentLocation;

    @ColumnComment("出生年份")
    private String birthday;

    @ColumnComment("身高")
    private Integer height;

    @ColumnComment("教育程度")
    private Long education;

    @ColumnComment("职业信息")
    private String profession;

    @ColumnComment("公司信息")
    private Long company;

    @ColumnComment("职位信息")
    private String position;

    @ColumnComment("学校类型")
    private Long schoolType;

    @Column(name="school")
    @ColumnComment("毕业学校")
    private String school;


    @ColumnComment("收入等级")
    private Long income;

    @ColumnComment("是否有车")
    private Long car;


    @ColumnComment("房产信息")
    private Long house;

    @ColumnComment("照片存储路径")
    private String photo;

    @ColumnComment("爱好信息")
    private String hobby;

    @ColumnComment("静态评分")
    private Integer score;

    @ColumnComment("标签信息")
    private String label;


    @ColumnComment("创建日期")
    private Date createdate = new Date();


    @ColumnComment("最后修改日期")
    private Date lastmodified = new Date();

    @ColumnComment("更多自我介绍")
    private String moreIntroduce;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getCompany() {
        return company;
    }

    public void setCompany(Long company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Long getIncome() {
        return income;
    }

    public void setIncome(Long income) {
        this.income = income;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Long getHouse() {
        return house;
    }

    public void setHouse(Long house) {
        this.house = house;
    }

    public Long getSchoolType() {
        return schoolType;
    }

    public void setSchoolType(Long schoolType) {
        this.schoolType = schoolType;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getMoreIntroduce() {
        return moreIntroduce;
    }

    public void setMoreIntroduce(String moreIntroduce) {
        this.moreIntroduce = moreIntroduce;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getGender() {
        return gender;
    }

    public void setGender(Long gender) {
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Long getEducation() {
        return education;
    }

    public void setEducation(Long education) {
        this.education = education;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public Long getCar() {
        return car;
    }

    public void setCar(Long car) {
        this.car = car;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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
}
