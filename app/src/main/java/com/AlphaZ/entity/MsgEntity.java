package com.AlphaZ.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * ProjectName: IM
 * PackageName: com.AlphaZ.entity
 * User: C0dEr
 * Date: 2017/5/26
 * Time: 下午2:15
 * Description:This is a class of com.AlphaZ.entity
 */
@Entity
@Table(name = "msg", schema = "alphaz", catalog = "")
public class MsgEntity extends BaseDTO{
    private Long id;
    private Integer fromwho;
    private Integer towho;
    private String msg;
    private Timestamp createtime;
    private Integer isread;
    private String username;
    private String type;
    private Integer avatar;


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "fromwho")
    public Integer getFromwho() {
        return fromwho;
    }

    public void setFromwho(Integer fromwho) {
        this.fromwho = fromwho;
    }

    @Basic
    @Column(name = "towho")
    public Integer getTowho() {
        return towho;
    }

    public void setTowho(Integer towho) {
        this.towho = towho;
    }

    @Basic
    @Column(name = "msg")
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Basic
    @Column(name = "createtime")
    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MsgEntity msgEntity = (MsgEntity) o;

        if (id != msgEntity.id) return false;
        if (fromwho != null ? !fromwho.equals(msgEntity.fromwho) : msgEntity.fromwho != null) return false;
        if (towho != null ? !towho.equals(msgEntity.towho) : msgEntity.towho != null) return false;
        if (msg != null ? !msg.equals(msgEntity.msg) : msgEntity.msg != null) return false;
        if (createtime != null ? !createtime.equals(msgEntity.createtime) : msgEntity.createtime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.intValue();
        result = 31 * result + (fromwho != null ? fromwho.hashCode() : 0);
        result = 31 * result + (towho != null ? towho.hashCode() : 0);
        result = 31 * result + (msg != null ? msg.hashCode() : 0);
        result = 31 * result + (createtime != null ? createtime.hashCode() : 0);
        return result;
    }

    @Basic
    @Column(name = "isread")
    public Integer getIsread() {
        return isread;
    }

    public void setIsread(Integer isread) {
        this.isread = isread;
    }

    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "avatar")
    public Integer getAvatar() {
        return avatar;
    }

    public void setAvatar(Integer avatar) {
        this.avatar = avatar;
    }
}
