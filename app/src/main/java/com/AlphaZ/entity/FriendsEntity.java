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
@Table(name = "friends", schema = "alphaz", catalog = "")
public class FriendsEntity extends BaseDTO {
    private Long id;
    private Integer userid;
    private Integer friendid;
    private Timestamp createtime;
    private String group;
    private String username;
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
    @Column(name = "userid")
    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    @Basic
    @Column(name = "friendid")
    public Integer getFriendid() {
        return friendid;
    }

    public void setFriendid(Integer friendid) {
        this.friendid = friendid;
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

        FriendsEntity that = (FriendsEntity) o;

        if (id != that.id) return false;
        if (userid != null ? !userid.equals(that.userid) : that.userid != null) return false;
        if (friendid != null ? !friendid.equals(that.friendid) : that.friendid != null) return false;
        if (createtime != null ? !createtime.equals(that.createtime) : that.createtime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.intValue();
        result = 31 * result + (userid != null ? userid.hashCode() : 0);
        result = 31 * result + (friendid != null ? friendid.hashCode() : 0);
        result = 31 * result + (createtime != null ? createtime.hashCode() : 0);
        return result;
    }

    @Basic
    @Column(name = "`group`")
    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
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
}
