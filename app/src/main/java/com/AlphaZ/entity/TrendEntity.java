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
@Table(name = "trend", schema = "alphaz", catalog = "")
public class TrendEntity extends BaseDTO {
    private Long id;
    private String note;
    private Integer fromwho;
    private String username;
    private Integer avatar;
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
    @Column(name = "note")
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TrendEntity that = (TrendEntity) o;

        if (id != that.id) return false;
        if (note != null ? !note.equals(that.note) : that.note != null) return false;
        if (fromwho != null ? !fromwho.equals(that.fromwho) : that.fromwho != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.intValue();
        result = 31 * result + (note != null ? note.hashCode() : 0);
        result = 31 * result + (fromwho != null ? fromwho.hashCode() : 0);
        return result;
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
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
    @Column(name = "createtime")
    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }
}
