package com.xunli.manager.web;

import java.util.List;
import java.util.Date;

/**
 * Created by shihj on 2017/9/13.
 */
public class ObjectData {
    private String id;

    private String name;

    private String pinYin;

    private Integer status;
    private Integer orderId;
    private String createAccount;
    private Date createTime;
    private String modifyAccount;
    private Date modifyTime;
    private Double gisBd09Lat;
    private Double gisBd09Lng;
    private Double gisGcj02Lat;
    private Double gisGcj02Lng;
    private Integer stubGroupCnt;

    private List<ObjectData> cityList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ObjectData> getCityList() {
        return cityList;
    }

    public void setCityList(List<ObjectData> cityList) {
        this.cityList = cityList;
    }

    public String getPinYin() {
        return pinYin;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setModifyAccount(String modifyAccount) {
        this.modifyAccount = modifyAccount;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public void setPinYin(String pinYin) {
        this.pinYin = pinYin;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getCreateAccount() {
        return createAccount;
    }

    public void setCreateAccount(String createAccount) {
        this.createAccount = createAccount;
    }

    public String getModifyAccount() {
        return modifyAccount;
    }

    public Double getGisBd09Lat() {
        return gisBd09Lat;
    }

    public void setGisBd09Lat(Double gisBd09Lat) {
        this.gisBd09Lat = gisBd09Lat;
    }

    public Double getGisBd09Lng() {
        return gisBd09Lng;
    }

    public void setGisBd09Lng(Double gisBd09Lng) {
        this.gisBd09Lng = gisBd09Lng;
    }

    public Double getGisGcj02Lat() {
        return gisGcj02Lat;
    }

    public void setGisGcj02Lat(Double gisGcj02Lat) {
        this.gisGcj02Lat = gisGcj02Lat;
    }

    public Double getGisGcj02Lng() {
        return gisGcj02Lng;
    }

    public void setGisGcj02Lng(Double gisGcj02Lng) {
        this.gisGcj02Lng = gisGcj02Lng;
    }

    public Integer getStubGroupCnt() {
        return stubGroupCnt;
    }

    public void setStubGroupCnt(Integer stubGroupCnt) {
        this.stubGroupCnt = stubGroupCnt;
    }
}
