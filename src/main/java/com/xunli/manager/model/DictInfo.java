package com.xunli.manager.model;

import javax.persistence.*;

/**
 * Created by Betty on 2017/7/16.
 */
@Entity
@Table(name = "DICT_INFO")
public class DictInfo {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dict_type")
    private String dictType;

    @Column(name = "dict_value")
    private String dictValue;

    @Column(name = "dict_desc")
    private String dictDesc;

    @Override
    public String toString()
    {
        return String.format("{id = %s,dictType = %s,dictValue = %s,dictDesc = %s}",id,dictType,dictValue,dictDesc);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    public String getDictDesc() {
        return dictDesc;
    }

    public void setDictDesc(String dictDesc) {
        this.dictDesc = dictDesc;
    }
}
