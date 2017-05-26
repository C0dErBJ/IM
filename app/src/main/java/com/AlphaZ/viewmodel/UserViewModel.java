package com.AlphaZ.viewmodel;

import java.util.List;
import java.util.Map;

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

    public String name;

    public String password;

    public Long avatar;

    public Map<Integer, String> role;

    public Map<String, List<String>> permission;

    public Map<String, List<String>> menuKV;


}
