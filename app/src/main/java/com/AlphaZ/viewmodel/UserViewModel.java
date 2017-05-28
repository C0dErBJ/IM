package com.AlphaZ.viewmodel;

/**
 * ProjectName: AlphaZ
 * PackageName: com.AlphaZ.viewmodel
 * User: C0dEr
 * Date: 2017/1/3
 * Time: 下午2:41
 * Description:This is a class of com.AlphaZ.viewmodel
 */
public class UserViewModel {

    public Long userid;

    public String username;

    public String password;

    public String newpassword;

    public Long avatar;

    public String phone;

    public String gender;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String state;

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public Long getAvatar() {
        return avatar;
    }

    public void setAvatar(Long avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
