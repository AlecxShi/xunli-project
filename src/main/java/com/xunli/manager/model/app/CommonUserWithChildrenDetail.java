package com.xunli.manager.model.app;

import java.util.List;

/**
 * Created by shihj on 2017/9/13.
 */
public class CommonUserWithChildrenDetail {
    
    private String token;

    private Long id;

    private String name;

    private Long parentId;

    private Long gender;

    private String bornLocation;

    private String currentLocation;

    private String birthday;

    private Integer height;

    private Long education;

    private String profession;

    private Long company;

    private String position;

    private Long schoolType;

    private String school;

    private Long income;

    private Long car;

    private Long house;

    private List<String> photo;  // 更多照片

    private String userImage;  // 用户头像

    private String hobby;

    private Integer score;

    private List<Object> label;  // 企业高管，多才多艺

    private Boolean isCollected;

    private String moreIntroduce;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
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

    public Long getIncome() {
        return income;
    }

    public void setIncome(Long income) {
        this.income = income;
    }

    public Long getCar() {
        return car;
    }

    public void setCar(Long car) {
        this.car = car;
    }

    public Long getHouse() {
        return house;
    }

    public void setHouse(Long house) {
        this.house = house;
    }

    public List<String> getPhoto() {
        return photo;
    }

    public void setPhoto(List<String> photo) {
        this.photo = photo;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public List<Object> getLabel() {
        return label;
    }

    public void setLabel(List<Object> label) {
        this.label = label;
    }

    public Boolean getIsCollected() {
        return isCollected;
    }

    public void setIsCollected(Boolean collected) {
        isCollected = collected;
    }

    public String getMoreIntroduce() {
        return moreIntroduce;
    }

    public void setMoreIntroduce(String moreIntroduce) {
        this.moreIntroduce = moreIntroduce;
    }
}
