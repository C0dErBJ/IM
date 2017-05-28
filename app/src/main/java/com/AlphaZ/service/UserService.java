package com.AlphaZ.service;

import com.AlphaZ.entity.api.ResponseModel;
import com.AlphaZ.viewmodel.UserViewModel;

import java.util.List;
import java.util.Map;

/**
 * ProjectName: AlphaZ
 * PackageName: com.AlphaZ.service
 * User: C0dEr
 * Date: 2016-11-10
 * Time: 14:57
 * Description:
 */
public interface UserService {

    ResponseModel login(String username, String password);

    UserViewModel fetchCurrentUser();

    ResponseModel changPassword(String username, String password, String newpassword);

}
