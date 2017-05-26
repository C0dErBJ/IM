package com.AlphaZ.service;

import com.AlphaZ.entity.api.ResponseModel;
import com.AlphaZ.viewmodel.UserUpdateViewModel;
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

    ResponseModel addUser(UserUpdateViewModel userUpdateViewModel);

    ResponseModel changPassword(String username, String password, String newpassword);

    Map<String, List<String>> getRoleByUserID(long userid);

    ResponseModel getUserListByParams(int pagesize, int pageno, String username);

    ResponseModel getRoleByRoleID(Long roleid);

    ResponseModel setRoleAndAuth(long roleid, String[] mpid, long userid);

    ResponseModel getAllAuth();

    ResponseModel getAuthByRoleid(long roleid);

    ResponseModel deleteUser(String username);

    ResponseModel deleteUserById(Long id);

    ResponseModel getUser(String username);

    ResponseModel getUserById(Long id);

    ResponseModel updateUserInfo(UserUpdateViewModel userUpdateViewModel);

    ResponseModel updateUserPwd(UserUpdateViewModel userUpdateViewModel);

    ResponseModel addRole(String rolename, String description);

    ResponseModel getRoleIdByUserId(Long id);

    ResponseModel deleteRole(Long id);

    ResponseModel updateRole(Long id, String roleName, String description);

    ResponseModel getRole();

    ResponseModel getMenu();

    ResponseModel getMenuByUserId(Long userId);

    ResponseModel getOperation();

    boolean isSystemRole(Long userid);
}
