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
@Table(name = "note", schema = "alphaz", catalog = "")
public class NoteEntity  extends BaseDTO{
    private Long id;
    private String note;
    private Integer fromwho;
    private Integer towho;
    private String usename;
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

        NoteEntity that = (NoteEntity) o;

        if (id != that.id) return false;
        if (note != null ? !note.equals(that.note) : that.note != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.intValue();
        result = 31 * result + (note != null ? note.hashCode() : 0);
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
    @Column(name = "towho")
    public Integer getTowho() {
        return towho;
    }

    public void setTowho(Integer towho) {
        this.towho = towho;
    }

    @Basic
    @Column(name = "usename")
    public String getUsename() {
        return usename;
    }

    public void setUsename(String usename) {
        this.usename = usename;
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
