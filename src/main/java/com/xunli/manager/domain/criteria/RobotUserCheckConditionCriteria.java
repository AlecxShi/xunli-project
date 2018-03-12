package com.xunli.manager.domain.criteria;

/**
 * Created by shihj on 2018/1/10.
 */
public class RobotUserCheckConditionCriteria {

    private String opType;//1 - 未登录时,且只填写了2个地址和性别;2 - 填写了年龄学历时;

    private String currentLocation;

    private String bornLocation;

    private Long gender;

    private String birthday;

    private Long education;

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getBornLocation() {
        return bornLocation;
    }

    public void setBornLocation(String bornLocation) {
        this.bornLocation = bornLocation;
    }

    public Long getGender() {
        return gender;
    }

    public void setGender(Long gender) {
        this.gender = gender;
    }

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
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
}
