package com.xunli.manager.domain.criteria;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by shihj on 2017/9/6.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateChildrenInfo implements Serializable{

    private final static long serialVersionUID = 1L;

    //用户token
    private String token;

    //子女公司情况
    private Long company;

    //子女职位信息
    private String position;

    //收入情况
    private Long income;

    //身高情况
    private Integer height;

    //房产情况
    private Long house;

    //车辆情况
    private  Long car;

    //学校情况
    private Long schoolType;

    //毕业学校名称
    private String school;

    //爱好
    private String hobby;

    //更多自我介绍
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

    public Long getCar() {
        return car;
    }

    public void setCar(Long car) {
        this.car = car;
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
}
