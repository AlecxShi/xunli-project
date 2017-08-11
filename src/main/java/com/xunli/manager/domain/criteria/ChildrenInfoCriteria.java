package com.xunli.manager.domain.criteria;

import com.xunli.manager.model.DictInfo;

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
    private String currentCity;
    //出生日期起始年份,目标性别为男,则为子女出生年-8;目标性别为女,则为子女出生年-15
    private String startBirthday;
    //出生日期终止年份,目标性别为男,则为子女出生年+15;目标性别为女,则为子女出生年+8
    private String endBirthday;
    //目标子女学历,必须大于等于该用户子女学历
    private DictInfo education;
}
