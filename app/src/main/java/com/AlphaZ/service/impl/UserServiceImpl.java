package com.AlphaZ.service.impl;

import com.AlphaZ.constant.SessionConstant;
import com.AlphaZ.constant.StatusCode;
import com.AlphaZ.dao.*;
import com.AlphaZ.entity.AlphazUserEntity;
import com.AlphaZ.entity.api.ResponseModel;
import com.AlphaZ.service.UserService;
import com.AlphaZ.util.encrypt.EncryptUtil;
import com.AlphaZ.util.string.DateUtil;
import com.AlphaZ.util.valid.ValideHelper;
import com.AlphaZ.viewmodel.UserViewModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    /**
     * 登录
     * String username 用户名
     * String password 密码
     */
    @Override
    public ResponseModel login(String name, String password) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.message = "登录失败";
        responseModel.statusCode = StatusCode.FAIL;
        if (ValideHelper.isNullOrEmpty(name) || ValideHelper.isNullOrEmpty(password)) {
            responseModel.message = "请填写用户名以及密码";
            return responseModel;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("password", password);
        params.put("status", StatusCode.SUCCESS);
        List<AlphazUserEntity> userlist = userDAO.findByFields(AlphazUserEntity.class, params);
        if (userlist.size() != 1) {
            return new ResponseModel("数据异常", StatusCode.FAIL, "");
        }
        AlphazUserEntity userEntity = userlist.get(0);
        userEntity.setUpdatetime(DateUtil.getTime());
        userEntity.setState(StatusCode.SUCCESS);
        userDAO.createOrUpdate(userEntity);
        UserViewModel model = userDAO.login(name, password);
        if (model == null) {
            responseModel.message = "用户名或密码错误";
            return responseModel;
        }
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
        model.statusCode = StatusCode.SUCCESS;
        model.message = "成功";
        return model;
    }

    /**
     * 获取保存在session里面用户的信息，本方法只适用于使用session来保存用户信息的情况
     * 这个方法凌驾于权限判断，所以仅限service层调用
     *
     * @return
     */
    @Override
    public UserViewModel fetchCurrentUser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        UserViewModel currentUser = null;
        if (session.getAttribute(SessionConstant.CURRENTUSER) != null) {
            currentUser = (UserViewModel) session.getAttribute(SessionConstant.CURRENTUSER);
        }
        return currentUser;
    }



}
