package com.xunli.manager.domain.criteria;

/**
 * Created by shihj on 2018/1/10.
 */
public class RobotUserCheckConditionCriteria {

    private String currentLocation;

    private String bornLocation;

    private Long gender;

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
}
