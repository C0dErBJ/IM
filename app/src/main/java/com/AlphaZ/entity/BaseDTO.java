package com.AlphaZ.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Version;


/**
 * 持久化对象的基类,所有要持久化到数据库的对象继承该类或其子类。由于持久化层级联的查询
 * 效率较低,所以DTO只能包含基本的java类型数据,暂不支持DTO嵌套DTO.若子类dto
 * 需要直接传输到前台的话,则其基本数据类型的属性必须有值(不能是null),否则从
 * json对象转化为javabean会失败，如果其属性是包装类则没有此限制.
 *
 * @author HuangJian
 * @since May 19, 2010
 */
public abstract class BaseDTO {

    public BaseDTO() {
    }

    @Version
    protected Integer entityVersion;

    public abstract Long getId();

    @JsonIgnore
    public int getEntityVersion() {
        return entityVersion;
    }


    public void setEntityVersion(int version) {
        this.entityVersion = version;
    }

}
