package com.AlphaZ.service.impl;

import com.AlphaZ.annotation.Auth;
import com.AlphaZ.constant.DataPrivilege;
import com.AlphaZ.constant.DataValidityConstant;
import com.AlphaZ.constant.MenuPrivilege;
import com.AlphaZ.constant.StatusCode;
import AlphaZ.dao.*;
import AlphaZ.entity.*;
import com.AlphaZ.dao.*;
import com.AlphaZ.entity.*;
import com.AlphaZ.entity.api.ResponseModel;
import com.AlphaZ.service.UserService;
import com.AlphaZ.util.caculate.MathUtil;
import com.AlphaZ.util.encrypt.EncryptUtil;
import com.AlphaZ.util.string.DateUtil;
import com.AlphaZ.util.valid.ValideHelper;
import AlphaZ.viewmodel.*;
import com.AlphaZ.viewmodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ProjectName: AlphaZ
 * PackageName: com.AlphaZ.service.impl
 * User: C0dEr
 * Date: 2016-11-10
 * Time: 14:58
 * Description:
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Resource
    UserDAO userDAO;
    @Resource
    UserRoleEntityDAO userRoleEntityDAO;
    @Resource
    MenuEntityDAO menuEntityDAO;
    @Resource
    OperationEntityDAO operationEntityDAO;
    @Resource
    MenuOperationEntityDAO menuOperationEntityDAO;
    @Resource
    RoleMenuOperationEntityDAO roleMenuOperationEntityDAO;
    @Resource
    RoleEntityDAO roleEntityDAO;

    /**
     * 登录
     * String username 用户名
     * String password 密码
     */
    @Override
    @Auth(value = DataPrivilege.CREATE, menu = MenuPrivilege.AUTH)
    public ResponseModel login(String name, String password) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.message = "登录失败";
        responseModel.statusCode = StatusCode.FAIL;
        if (ValideHelper.isNullOrEmpty(name) || ValideHelper.isNullOrEmpty(password)) {
            responseModel.message = "请填写用户名以及密码";
            return responseModel;
        }
        try {
            password = EncryptUtil.EncoderByMd5(password);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("password", password);
        params.put("status", DataValidityConstant.INUSE);
        List<AlphazUserEntity> userlist = userDAO.findByFields(AlphazUserEntity.class, params);
        if (userlist.size() != 1) {
            return new ResponseModel("数据异常", StatusCode.FAIL, "");
        }
        AlphazUserEntity userEntity = userlist.get(0);
        userEntity.setUpdatetime(DateUtil.getTime());
        userDAO.createOrUpdate(userEntity);
        UserViewModel model = userDAO.login(name, password);
        if (model == null) {
            responseModel.message = "用户名或密码错误";
            return responseModel;
        }
        model.permission = userDAO.getRoleByUserID(model.userid);
        model.menuKV = userDAO.getMenuKVByUserID(model.userid);
        responseModel.setData(model);
        responseModel.message = "登录成功";
        responseModel.statusCode = StatusCode.SUCCESS;
        return responseModel;
    }

    /**
     * 修改密码
     * String username    用户名
     * String password    旧密码
     * String newpassword 新密码
     */
    @Override
    @Auth(value = DataPrivilege.UPDATE, menu = MenuPrivilege.USER)
    public ResponseModel changPassword(String username, String password, String newpassword) {
        ResponseModel model = new ResponseModel();
        if (ValideHelper.isNullOrEmpty(username) || ValideHelper.isNullOrEmpty(password) || ValideHelper.isNullOrEmpty(newpassword)) {
            model.setStatusCode(StatusCode.FAIL);
            model.setMessage("输入为空");
            return model;
        }
        Map<String, Object> params = new HashMap<>();
        try {
            password = EncryptUtil.EncoderByMd5(password);
            newpassword = EncryptUtil.EncoderByMd5(newpassword);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        params.put("username", username);
        params.put("password", password);
        AlphazUserEntity user = (AlphazUserEntity) userDAO.findByFields(AlphazUserEntity.class, params);
        user.setPassword(newpassword);
        user = userDAO.createOrUpdate(user);
        ResponseUserViewModel userViewModel = new ResponseUserViewModel(user);
        model.statusCode = StatusCode.SUCCESS;
        model.message = "成功";
        model.data = userViewModel;
        return model;
    }

    /**
     * 更新用户信息
     *
     * @param userUpdateViewModel
     * @return
     */
    @Override
    @Auth(value = DataPrivilege.UPDATE, menu = MenuPrivilege.USER)
    public ResponseModel updateUserInfo(UserUpdateViewModel userUpdateViewModel) {
        ResponseModel model = new ResponseModel();
        if (ValideHelper.isNullOrEmpty(userUpdateViewModel)) {
            model.setStatusCode(StatusCode.FAIL);
            model.setMessage("输入为空");
            return model;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("id", userUpdateViewModel.getId());
        params.put("status", DataValidityConstant.INUSE);
        List<AlphazUserEntity> userList = userDAO.findByFields(AlphazUserEntity.class, params);
        if (userList.size() != 1) {
            model.setStatusCode(StatusCode.FAIL);
            model.setMessage("获取为空");
            return model;
        }
        AlphazUserEntity userEntity = userList.get(0);

        List<AlphazUserRoleEntity> rolelist = userRoleEntityDAO.findByUseridAndStatus(userUpdateViewModel.getId(), StatusCode.SUCCESS);
        List<AlphazUserRoleEntity> bjrolelist = new ArrayList<>();
        for (int i = 0; i < rolelist.size(); i++) {
            AlphazUserRoleEntity roleEntity = rolelist.get(i);
            roleEntity.setStatus(DataValidityConstant.DELETED);
            roleEntity.setUpdatetime(DateUtil.getTime());
            bjrolelist.add(roleEntity);
        }
        userRoleEntityDAO.save(bjrolelist);
        List<AlphazUserRoleEntity> userRoleList = new ArrayList<>();
        String role[] = userUpdateViewModel.getRoleval().split(",");
        for (int i = 0; i < role.length; i++) {
            AlphazUserRoleEntity userRoleEntity = new AlphazUserRoleEntity();
            userRoleEntity.setRoleid(Long.parseLong(role[i]));
            userRoleEntity.setUserid(userEntity.getId());
            userRoleEntity.setCreatetime(DateUtil.getTime());
            userRoleEntity.setUpdatetime(DateUtil.getTime());
            userRoleEntity.setStatus(DataValidityConstant.INUSE);
            userRoleList.add(userRoleEntity);
        }
        userRoleEntityDAO.save(userRoleList);
        userEntity.setName(userUpdateViewModel.getName());
        //userEntity.setMechanismname(userUpdateViewModel.getMechanismname());
        //userEntity.setMechanismid(userUpdateViewModel.getMechanismid());
        userEntity.setNote(userUpdateViewModel.getNote());
        userEntity.setUpdatetime(DateUtil.getTime());
        userDAO.createOrUpdate(userEntity);
        model.setStatusCode(StatusCode.SUCCESS);
        return model;

    }


    /**
     * 修改账户密码
     *
     * @param userUpdateViewModel
     * @return
     */
    @Override
    @Auth(value = DataPrivilege.UPDATEPWD, menu = MenuPrivilege.USER)
    public ResponseModel updateUserPwd(UserUpdateViewModel userUpdateViewModel) {
        if (ValideHelper.isNullOrEmpty(userUpdateViewModel)) {
            return new ResponseModel("获取为空", StatusCode.FAIL, null);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("id", userUpdateViewModel.getId());
        params.put("status", DataValidityConstant.INUSE);
        List<AlphazUserEntity> userList = userDAO.findByFields(AlphazUserEntity.class, params);
        if (userList.size() != 1) {
            return new ResponseModel("获取为空", StatusCode.FAIL, null);
        }
        AlphazUserEntity userEntity = userList.get(0);
        String password = userUpdateViewModel.getPassword();
        String newpassword = userUpdateViewModel.getNewpassword();
        try {
            password = EncryptUtil.EncoderByMd5(password);
            newpassword = EncryptUtil.EncoderByMd5(newpassword);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (!userEntity.getPassword().equals(password)) {
            return new ResponseModel("密码输入错误", StatusCode.FAIL, null);
        }
        userEntity.setPassword(newpassword);
        userEntity.setUpdatetime(DateUtil.getTime());
        userDAO.createOrUpdate(userEntity);
        return new ResponseModel("修改成功", StatusCode.SUCCESS, null);
    }

    /**
     * 获取保存在session里面用户的信息，本方法只适用于使用session来保存用户信息的情况
     * 这个方法凌驾于权限判断，所以仅限service层调用
     *
     * @return
     */
    @Override
    public UserViewModel fetchCurrentUser() {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        HttpSession session = request.getSession();
//        UserViewModel currentUser = null;
//        if (session.getAttribute(SessionConstant.CURRENTUSER) != null) {
//            currentUser = (UserViewModel) session.getAttribute(SessionConstant.CURRENTUSER);
//        }
//        return currentUser;
        return null;
    }

    /**
     * 查询权限信息，如果没有指定查看哪一条，默认返回第一条信息。
     *
     * @return
     */
    @Override
    public ResponseModel getRoleByRoleID(Long roleid) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.message = "查询失败";
        responseModel.statusCode = StatusCode.FAIL;
        List<AlphazRoleEntity> roleEntities = roleEntityDAO.findByField(AlphazRoleEntity.class, "status", DataValidityConstant.INUSE);
        List<RoleSMSModel> rsmsList = new ArrayList<RoleSMSModel>();
        for (AlphazRoleEntity roleEntity : roleEntities) {
            RoleSMSModel rsms = new RoleSMSModel();
            rsms.roleid = roleEntity.getId();
            rsms.roleName = roleEntity.getRolename();
            rsmsList.add(rsms);
        }
        if (rsmsList.size() == 0) {
            responseModel.message = "暂无数据";
            return responseModel;
        }
        if (roleid == null || roleid == 0) {
            roleid = rsmsList.get(0).roleid;
        }
        Map<String, List<String>> roleINF = roleEntityDAO.getRoleByRoleID(roleid);
        ResponseUserRoleViewModle userRole = new ResponseUserRoleViewModle();
        userRole.allRole = rsmsList;
        //userRole.roleINF = roleINF;
        responseModel.message = "查询成功";
        responseModel.statusCode = StatusCode.SUCCESS;
        responseModel.data = userRole;
        return responseModel;
    }

    @Override
    public ResponseModel setRoleAndAuth(long roleid, String[] mpid, long userid) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.message = "操作失败";
        responseModel.statusCode = StatusCode.FAIL;
        String[] rmpid = roleEntityDAO.getMPidByRoleid(roleid);
        String[] mpidintersect = MathUtil.intersect(rmpid, mpid);
        String[] removempid = MathUtil.substract(rmpid, mpidintersect);
        String[] addmpid = MathUtil.substract(mpid, mpidintersect);
        for (String s : removempid) {
            String[] a = s.split("-");
            boolean isSuccess = setRoleRemoveAuth(roleid, Long.parseLong(a[0]), Long.parseLong(a[1]), userid);
            if (!isSuccess) {
                return responseModel;
            }
        }
        for (String s : addmpid) {
            String[] a = s.split("-");
            boolean isSuccess = setRoleAddAuth(roleid, Long.parseLong(a[0]), Long.parseLong(a[1]), userid);
            if (!isSuccess) {
                return responseModel;
            }
        }
        List<UserMenuViewModel> umvm = roleEntityDAO.getMenuByRoleId(roleid);
        for (UserMenuViewModel um : umvm) {
            if (um.parentId != 0) {
                Optional<UserMenuViewModel> u = umvm.stream().filter(a -> a.menuId == um.parentId).findFirst();
                if (!u.isPresent()) {
                    AlphaRoleMenuOperationEntity al = new AlphaRoleMenuOperationEntity();
                    al.setRoleid(roleid);
                    //todo 待优化
                    if (um.parentId == 4) {
                        al.setMenuid(8l);
                    } else if (um.parentId == 5) {
                        al.setMenuid(25l);
                    } else if (um.parentId == 12) {
                        al.setMenuid(41l);
                    }
                    al.setStatus(StatusCode.SUCCESS);
                    roleMenuOperationEntityDAO.save(al);
                }
            } else if (ValideHelper.isNullOrEmpty(um.url) && !umvm.stream().anyMatch(a -> a.parentId == um.menuId)) {
                roleMenuOperationEntityDAO.delete(um.armId);
            }
        }
        responseModel.message = "添加成功";
        responseModel.statusCode = StatusCode.SUCCESS;
        return responseModel;
    }

    public boolean setRoleAddAuth(long roleid, long menuid, long operationid, long userid) {

        Long mpid = null;
        List<AlphazMenuOperationEntity> mplist = menuOperationEntityDAO.findByMenuidAndOperationId(menuid, operationid);
        if (mplist.size() == 0) {
            AlphazMenuOperationEntity mp = new AlphazMenuOperationEntity();
            mp.setMenuid(menuid);
            mp.setOperationId(operationid);
            mp.setCreateby(userid);
            mp.setUpdateby(userid);
            mp.setStatus(DataValidityConstant.INUSE);
            mp.setCreatetime(DateUtil.getTime());
            mp.setUpdatetime(DateUtil.getTime());
            mp = menuOperationEntityDAO.save(mp);
            mpid = mp.getId();
        } else {
            AlphazMenuOperationEntity mp = mplist.get(0);
            if (mp.getStatus() == 0) {
                mpid = mp.getId();
            } else {
                mpid = mp.getId();
                mp.setStatus(DataValidityConstant.INUSE);
                menuOperationEntityDAO.saveAndFlush(mp);
            }
        }
        List<AlphaRoleMenuOperationEntity> rmolist = roleMenuOperationEntityDAO.findByMenuidAndRoleid(mpid, roleid);
        if (rmolist.size() == 0) {
            AlphaRoleMenuOperationEntity rmo = new AlphaRoleMenuOperationEntity();
            rmo.setRoleid(roleid);
            rmo.setMenuid(mpid);
            rmo.setCreateby(userid);
            rmo.setStatus(DataValidityConstant.INUSE);
            rmo.setCreatetime(DateUtil.getTime());
            rmo.setUpdateby(userid);
            rmo.setUpdatetime(DateUtil.getTime());
            roleMenuOperationEntityDAO.save(rmo);
        } else {
            AlphaRoleMenuOperationEntity rmo = rmolist.get(0);
            if (rmo.getStatus() == DataValidityConstant.INUSE) {
                return false;
            } else {
                rmo.setStatus(DataValidityConstant.INUSE);
                rmo.setUpdateby(userid);
                rmo.setUpdatetime(DateUtil.getTime());
                roleMenuOperationEntityDAO.save(rmo);
            }
        }
        return true;
    }

    public boolean setRoleRemoveAuth(long roleid, long menuid, long operationid, long userid) {
        Long mpid = null;
        List<AlphazMenuOperationEntity> mplist = menuOperationEntityDAO.findByMenuidAndOperationId(menuid, operationid);
        if (mplist.size() == 0) {
            return false;
        } else {
            AlphazMenuOperationEntity mp = mplist.get(0);
            if (mp.getStatus() == 0) {
                mpid = mp.getId();
            }
        }
        List<AlphaRoleMenuOperationEntity> rmolist = roleMenuOperationEntityDAO.findByMenuidAndRoleid(mpid, roleid);
        if (rmolist.size() == 0) {
            return false;
        } else {
            AlphaRoleMenuOperationEntity rmo = rmolist.get(0);
            if (rmo.getStatus() == DataValidityConstant.INUSE) {
                rmo.setStatus(DataValidityConstant.DELETED);
                roleMenuOperationEntityDAO.save(rmo);
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public ResponseModel getAllAuth() {
        ResponseModel responseModel = new ResponseModel();
        responseModel.message = "查询失败";
        responseModel.statusCode = StatusCode.FAIL;
        List<AlphazRoleEntity> roleEntities = roleEntityDAO.findByField(AlphazRoleEntity.class, "status", DataValidityConstant.INUSE);
        List<RoleSMSModel> rsmsList = new ArrayList<RoleSMSModel>();
        for (AlphazRoleEntity roleEntity : roleEntities) {
            RoleSMSModel rsms = new RoleSMSModel();
            rsms.roleid = roleEntity.getId();
            rsms.roleName = roleEntity.getRolename();
            rsms.description = roleEntity.getDescription();
            rsms.issystemrole = roleEntity.getIssystem();
            rsmsList.add(rsms);
        }
        if (rsmsList.size() == 0) {
            responseModel.message = "暂无数据";
            return responseModel;
        }
        List<UserRoleModel> roleINF = roleEntityDAO.getRoleAndAuth();

        ResponseUserRoleViewModle userRole = new ResponseUserRoleViewModle();
        userRole.allRole = rsmsList;
        userRole.roleINF = roleINF;
        responseModel.message = "查询成功";
        responseModel.statusCode = StatusCode.SUCCESS;
        responseModel.data = userRole;
        return responseModel;
    }

    @Override
    public ResponseModel getAuthByRoleid(long roleid) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.message = "查询失败";
        responseModel.statusCode = StatusCode.FAIL;
        List<UserRoleModel> roleINF = roleEntityDAO.getRoleAndAuth();
        List<UserRoleModel> urlist = roleEntityDAO.getAuthByRoleid(roleid);
        for (UserRoleModel userRoleModel : roleINF) {
            for (UserRoleModel roleModel : urlist) {
                if (userRoleModel.menuid == roleModel.menuid) {
                    for (OperationModel operationModel : userRoleModel.operation) {
                        for (OperationModel model : roleModel.operation) {
                            if (model.id == operationModel.id) {
                                operationModel.havaAuth = true;
                            }
                        }
                    }
                }
            }
        }
        responseModel.statusCode = StatusCode.SUCCESS;
        responseModel.data = roleINF;
        return responseModel;
    }

    public Map<String, List<String>> getRoleByUserID(long userid) {
        return userDAO.getRoleByUserID(userid);
    }


    /**
     * 遍历出所有的角色 role
     */
    @Override
    public ResponseModel getRole() {
        ResponseModel model = new ResponseModel();
        Map<String, Object> params = new HashMap<>();
        params.put("status", StatusCode.SUCCESS);
        if (!this.fetchCurrentUser().role.containsValue("超级管理员")) {
            params.put("issystem", StatusCode.FAIL);
        }
        List<AlphazRoleEntity> roleEntityList = roleEntityDAO.findByFields(AlphazRoleEntity.class, params);
        model.setStatusCode(StatusCode.SUCCESS);
        model.setData(roleEntityList);
        return model;
    }

    @Override
    public ResponseModel getRoleIdByUserId(Long id) {
        ResponseModel model = new ResponseModel();
        List<Integer> list = userRoleEntityDAO.getroleidbyuserid(id);
        if (!ValideHelper.isNullOrEmpty(list)) {
            List<RoleUserViewModel> rolelist = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                RoleUserViewModel viewModel = new RoleUserViewModel(list.get(i));
                rolelist.add(viewModel);
            }
            model.setStatusCode(StatusCode.SUCCESS);
            model.setData(rolelist);
            return model;
        }
        model.setStatusCode(StatusCode.SUCCESS);
        return model;
    }


    /**
     * 分页查询用户信息
     *
     * @param pagesize 每页几条
     * @param pageno   页数
     * @param username 用户名
     * @return
     */
    @Override
    @Auth(value = DataPrivilege.SELECT, menu = MenuPrivilege.USER)
    public ResponseModel getUserListByParams(int pagesize, int pageno, String username) {
        ResponseModel model = new ResponseModel();
        if (ValideHelper.isNullOrEmpty(pagesize) || ValideHelper.isNullOrEmpty(pageno)) {
            model.setStatusCode(StatusCode.FAIL);
            model.setMessage("输入为空");
            return model;
        }
        model.data = userDAO.getUserListByParams(pagesize, pageno, username);
        model.statusCode = StatusCode.SUCCESS;
        model.message = "成功";
        return model;
    }


    /**
     * 根据id删除用户
     *
     * @param id
     * @return
     */
    @Override
    @Auth(value = DataPrivilege.DELETE, menu = MenuPrivilege.USER)
    public ResponseModel deleteUserById(Long id) {
        ResponseModel model = new ResponseModel();
        if (ValideHelper.isNullOrEmpty(id)) {
            model.setStatusCode(StatusCode.FAIL);
            model.setMessage("输入为空");
            return model;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("status", DataValidityConstant.INUSE);
        List<AlphazUserEntity> userList = userDAO.findByFields(AlphazUserEntity.class, params);
        if (userList.size() != 0) {
            AlphazUserEntity user = userList.get(0);
            user.setUpdatetime(DateUtil.getTime());
            user.setStatus(StatusCode.FAIL);
            userDAO.createOrUpdate(user);
            model.statusCode = StatusCode.SUCCESS;
            return model;
        }
        model.statusCode = StatusCode.FAIL;
        model.setMessage("删除错误");
        return model;
    }


    /**
     * 删除账号
     *
     * @param username
     * @return
     */
    @Override
    @Auth(value = DataPrivilege.DELETE, menu = MenuPrivilege.USER)
    public ResponseModel deleteUser(String username) {
        ResponseModel model = new ResponseModel();
        if (ValideHelper.isNullOrEmpty(username)) {
            model.setStatusCode(StatusCode.FAIL);
            model.setMessage("输入为空");
            return model;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("status", DataValidityConstant.INUSE);
        List<AlphazUserEntity> userList = userDAO.findByFields(AlphazUserEntity.class, params);
        if (userList.size() != 0) {
            AlphazUserEntity user = userList.get(0);
            user.setStatus(StatusCode.FAIL);
            userDAO.createOrUpdate(user);
            model.statusCode = StatusCode.SUCCESS;
            return model;
        }
        model.statusCode = StatusCode.FAIL;
        model.setMessage("删除错误");
        return model;
    }

    /**
     * 根据id查询出用户
     *
     * @param id
     * @return
     */
    @Override
    @Auth(value = DataPrivilege.SELECT, menu = MenuPrivilege.USER)
    public ResponseModel getUserById(Long id) {
        ResponseModel model = new ResponseModel();
        if (ValideHelper.isNullOrEmpty(id)) {
            model.setStatusCode(StatusCode.FAIL);
            model.setMessage("输入为空");
            return model;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("status", DataValidityConstant.INUSE);
        List<AlphazUserEntity> userList = userDAO.findByFields(AlphazUserEntity.class, params);
        if (userList.size() != 0) {
            model.data = userList.get(0);
            model.statusCode = StatusCode.SUCCESS;
            return model;
        }
        model.statusCode = StatusCode.FAIL;
        return model;
    }


    /**
     * 获取到用户的详细信息
     *
     * @param username
     * @return
     */
    @Override
    @Auth(value = DataPrivilege.SELECT, menu = MenuPrivilege.USER)
    public ResponseModel getUser(String username) {
        ResponseModel model = new ResponseModel();
        if (ValideHelper.isNullOrEmpty(username)) {
            model.setStatusCode(StatusCode.FAIL);
            model.setMessage("输入为空");
            return model;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("status", StatusCode.SUCCESS);
        List<AlphazUserEntity> userList = userDAO.findByFields(AlphazUserEntity.class, params);
        if (userList.size() != 0) {
            model.data = userList.get(0);
            model.statusCode = StatusCode.SUCCESS;
            return model;
        }
        model.statusCode = StatusCode.FAIL;
        return model;
    }

    /**
     * 创建用户
     *
     * @param userUpdateViewModel
     * @return
     */
    @Override
    @Auth(value = DataPrivilege.CREATE, menu = MenuPrivilege.USER)
    public ResponseModel addUser(UserUpdateViewModel userUpdateViewModel) {
        ResponseModel model = new ResponseModel();
        if (ValideHelper.isNullOrEmpty(userUpdateViewModel)) {
            model.setStatusCode(StatusCode.FAIL);
            model.setMessage("输入为空");
            return model;
        }
        //判断用户名重复
        Map<String, Object> params = new HashMap<>();
        params.put("name", userUpdateViewModel.getName());
        params.put("status", DataValidityConstant.INUSE);
        List<AlphazUserEntity> userEntityList = userDAO.findByFields(AlphazUserEntity.class, params);
        if (userEntityList.size() != 0) {
            model.statusCode = StatusCode.FAIL;
            model.message = "用户名已注册";
            return model;
        }
        AlphazUserEntity user = new AlphazUserEntity();

        user.setName(userUpdateViewModel.getName());
        try {
            user.setPassword(EncryptUtil.EncoderByMd5("111111"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        user.setCreatetime(DateUtil.getTime());
        user.setCreateby(null);
        user.setUpdateby(null);
        user.setStatus(StatusCode.SUCCESS);
        user.setUpdatetime(DateUtil.getTime());
        user.setNote(userUpdateViewModel.getNote());
        user = userDAO.create(user);
        String role[] = userUpdateViewModel.getRoleval().split(",");
        List<AlphazUserRoleEntity> roleList = new ArrayList<>();
        for (int i = 0; i < role.length; i++) {
            System.out.println(role[i]);
            AlphazUserRoleEntity userRoleEntity = new AlphazUserRoleEntity();
            userRoleEntity.setRoleid(Long.parseLong(role[i]));
            userRoleEntity.setUserid(user.getId());
            userRoleEntity.setStatus(DataValidityConstant.INUSE);
            userRoleEntity.setCreatetime(DateUtil.getTime());
            userRoleEntity.setUpdatetime(DateUtil.getTime());
            roleList.add(userRoleEntity);
        }
        userRoleEntityDAO.save(roleList);
        model.statusCode = StatusCode.SUCCESS;
        return model;
    }

    /**
     * 管理员新增角色role
     *
     * @param rolename
     */
    @Override
    public ResponseModel addRole(String rolename, String description) {
        ResponseModel model = new ResponseModel();
        if (ValideHelper.isNullOrEmpty(rolename)) {
            model.setMessage("请输入角色名称");
            return model;
        }
        AlphazRoleEntity roleEntity = new AlphazRoleEntity();
        roleEntity.setRolename(rolename);
        roleEntity.setDescription(description);
        roleEntity.setIssystem(1);//新角色不是系统角色，默认1
        roleEntity.setPrivilgelevel(null);
        roleEntity.setCreateby(null);
        roleEntity.setUpdateby(null);
        roleEntity.setCreatetime(DateUtil.getTime());
        roleEntity.setUpdatetime(DateUtil.getTime());
        roleEntity.setStatus(StatusCode.SUCCESS);
        AlphazRoleEntity role = roleEntityDAO.create(roleEntity);
        model.data = new RoleSMSModel(role.getId(), role.getRolename(), role.getIssystem(), role.getDescription());
        model.setStatusCode(StatusCode.SUCCESS);
        model.setMessage("新增成功");
        return model;
    }

    /**
     * 管理员删除角色role
     * 1.删除role表
     * 2.删除user_role表
     * 3.删除role_menu_operation表
     *
     * @param id
     */
    @Override
    public ResponseModel deleteRole(Long id) {
        ResponseModel model = new ResponseModel();
        if (ValideHelper.isNullOrEmpty(id)) {
            model.setMessage("请提交用户id");
            return model;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        params.put("status", StatusCode.SUCCESS);
        List<AlphazRoleEntity> roleList = roleEntityDAO.findByFields(AlphazRoleEntity.class, params);
        AlphazRoleEntity roleEntity = roleList.get(0);
        Long roleId = roleEntity.getId();
        //1.删除role表
        roleEntity.setStatus(StatusCode.FAIL);
        roleEntityDAO.createOrUpdate(roleEntity);
        //2.删除user_role表

        List<AlphazUserRoleEntity> userRoleList = userRoleEntityDAO.findByRoleidAndStatus(roleId, StatusCode.SUCCESS);
        for (int i = 0; i < userRoleList.size(); i++) {
            userRoleList.get(i).setStatus(StatusCode.FAIL);
        }
        userRoleEntityDAO.save(userRoleList);
        //3.删除role_menu_operation表
        Map<String, Object> paramsRoleMenuOperation = new HashMap<String, Object>();
        paramsRoleMenuOperation.put("roleid", roleId);
        paramsRoleMenuOperation.put("status", StatusCode.SUCCESS);
        List<AlphaRoleMenuOperationEntity> roleMenuOperationList = roleMenuOperationEntityDAO.findByRoleidAndStatus(roleId, StatusCode.SUCCESS);
        for (int i = 0; i < roleMenuOperationList.size(); i++) {
            roleMenuOperationList.get(i).setStatus(StatusCode.FAIL);
        }
        roleMenuOperationEntityDAO.save(roleMenuOperationList);
        model.setStatusCode(StatusCode.SUCCESS);
        model.setMessage("删除成功");
        return model;
    }

    @Override
    public ResponseModel updateRole(Long id, String roleName, String description) {
        ResponseModel model = new ResponseModel();
        if (ValideHelper.isNullOrEmpty(roleName)) {
            model.setMessage("请输入用户名称");
            return model;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        params.put("status", StatusCode.SUCCESS);
        List<AlphazRoleEntity> roleList = roleEntityDAO.findByFields(AlphazRoleEntity.class, params);
        AlphazRoleEntity roleEntity = roleList.get(0);
        roleEntity.setDescription(description);
        roleEntity.setRolename(roleName);
        roleEntityDAO.createOrUpdate(roleEntity);
        model.setStatusCode(StatusCode.SUCCESS);
        model.data = new RoleSMSModel(roleEntity.getId(), roleEntity.getRolename(), roleEntity.getIssystem(), roleEntity.getDescription());
        return model;
    }

    /**
     * 遍历出所有的模块  menu
     */
    @Override
    public ResponseModel getMenu() {
        ResponseModel model = new ResponseModel();
        List<AlphazMenuEntity> menuEntityList = menuEntityDAO.findByStatus(StatusCode.SUCCESS);
        model.setStatusCode(StatusCode.SUCCESS);
        model.setData(menuEntityList);
        return model;
    }

    /**
     * 通过用户id获取他的菜单
     *
     * @param userId
     * @return
     */
    @Override
    public ResponseModel getMenuByUserId(Long userId) {
        ResponseModel model = new ResponseModel();
        List<UserMenuViewModel> menuEntityList = roleEntityDAO.getMenuByUserId(userId);
        List<TreeMenuViewModel> result = new ArrayList<>();
        List<UserMenuViewModel> parent = menuEntityList.stream().filter(a -> a.parentId == 0).collect(Collectors.toList());
        List<UserMenuViewModel> children = menuEntityList.stream().filter(a -> a.parentId != 0).collect(Collectors.toList());
        for (UserMenuViewModel umvm : parent) {
            TreeMenuViewModel menuViewModel = new TreeMenuViewModel();
            menuViewModel.menuId = umvm.menuId;
            menuViewModel.icon = umvm.icon;
            menuViewModel.menuName = umvm.menuName;
            menuViewModel.url = umvm.url;
            menuViewModel.order = umvm.order;
            result.add(menuViewModel);
        }
        for (UserMenuViewModel umvm : children) {
            for (TreeMenuViewModel prt : result) {
                if (prt.menuId.equals(umvm.parentId)) {
                    if (ValideHelper.isNullOrEmpty(prt.child)) {
                        prt.child = new ArrayList<>();
                        TreeMenuViewModel menuViewModel = new TreeMenuViewModel();
                        menuViewModel.menuId = umvm.menuId;
                        menuViewModel.icon = umvm.icon;
                        menuViewModel.menuName = umvm.menuName;
                        menuViewModel.url = umvm.url;
                        menuViewModel.order = umvm.order;
                        prt.child.add(menuViewModel);
                    } else {
                        TreeMenuViewModel menuViewModel = new TreeMenuViewModel();
                        menuViewModel.menuId = umvm.menuId;
                        menuViewModel.icon = umvm.icon;
                        menuViewModel.menuName = umvm.menuName;
                        menuViewModel.url = umvm.url;
                        menuViewModel.order = umvm.order;
                        prt.child.add(menuViewModel);
                    }
                }
            }
        }
        model.setData(result);
        model.setStatusCode(StatusCode.SUCCESS);
        model.setMessage("查询成功");
        return model;
    }

    /**
     * 遍历出所有的操作 operation
     */
    @Override
    public ResponseModel getOperation() {
        ResponseModel model = new ResponseModel();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", StatusCode.SUCCESS);
        List<AlphazOperationEntity> operationEntityList = operationEntityDAO.findByStatus(StatusCode.SUCCESS);
        model.setStatusCode(StatusCode.SUCCESS);
        model.setData(operationEntityList);
        return model;
    }

    @Override
    public boolean isSystemRole(Long userid) {
        return userDAO.isSystemRole(userid);
    }


}
