package com.AlphaZ.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * ProjectName: IM
 * PackageName: com.AlphaZ.entity
 * User: C0dEr
 * Date: 2017/5/30
 * Time: 下午1:06
 * Description:This is a class of com.AlphaZ.entity
 */
@Entity
@Table(name = "groupmsg", schema = "alphaz", catalog = "")
public class GroupmsgEntity extends BaseDTO {
    private Long id;
    private Integer fromwho;
    private Integer towho;
    private String msg;
    private Integer avatar;
    private String username;
    private Integer isread;
    private Integer hassend;
    private Timestamp createtime;


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
    @Column(name = "avatar")
    public Integer getAvatar() {
        return avatar;
    }

    public void setAvatar(Integer avatar) {
        this.avatar = avatar;
    }

    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupmsgEntity that = (GroupmsgEntity) o;

        if (id != that.id) return false;
        if (fromwho != null ? !fromwho.equals(that.fromwho) : that.fromwho != null) return false;
        if (towho != null ? !towho.equals(that.towho) : that.towho != null) return false;
        if (msg != null ? !msg.equals(that.msg) : that.msg != null) return false;
        if (avatar != null ? !avatar.equals(that.avatar) : that.avatar != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.intValue();
        result = 31 * result + (fromwho != null ? fromwho.hashCode() : 0);
        result = 31 * result + (towho != null ? towho.hashCode() : 0);
        result = 31 * result + (msg != null ? msg.hashCode() : 0);
        result = 31 * result + (avatar != null ? avatar.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
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
    @Column(name = "hassend")
    public Integer getHassend() {
        return hassend;
    }

    public void setHassend(Integer hassend) {
        this.hassend = hassend;
    }

    @Basic
    @Column(name = "createtime")
    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }
}
