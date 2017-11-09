package com.xunli.manager.domain.criteria;

import java.util.Date;

/**
 * Created by shihj on 2017/11/9.
 */
public class RobotUserLoginCriteria {
    private Date startTime;

    private Date endTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
