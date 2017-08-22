package com.xunli.manager.domain.criteria;

import com.xunli.manager.model.ChildrenInfoTwo;
import com.xunli.manager.model.DictInfo;

import java.util.List;

/**
 * Created by shihj on 2017/7/25.
 * use to generate children recommend info
 */
public class ChildrenInfoCriteria {

    //子女性别,直接设置为和用户子女性别相反
    private DictInfo gender;
    //家乡所在地,与用户子女所在地一致
    private String bornLocation;
    //当前所在地,与用户子女性别所在地一致
    private String currentLocation;
    //出生日期起始年份,目标性别为男,则为子女出生年-8;目标性别为女,则为子女出生年-15
    private String startBirthday;
    //出生日期终止年份,目标性别为男,则为子女出生年+15;目标性别为女,则为子女出生年+8
    private String endBirthday;
    //目标子女学历,必须大于等于该用户子女学历
    private List<Long> education;
    //获取数量
    private Integer num;
    //需要排除的,避免重复
    private List<ChildrenInfoTwo> except;

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

    public String getStartBirthday() {
        return startBirthday;
    }

    public void setStartBirthday(String startBirthday) {
        this.startBirthday = startBirthday;
    }

    public String getEndBirthday() {
        return endBirthday;
    }

    public void setEndBirthday(String endBirthday) {
        this.endBirthday = endBirthday;
    }

    public List<Long> getEducation() {
        return education;
    }

    public void setEducation(List<Long> education) {
        this.education = education;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public List<ChildrenInfoTwo> getExcept() {
        return except;
    }

    public void setExcept(List<ChildrenInfoTwo> except) {
        this.except = except;
    }
}
