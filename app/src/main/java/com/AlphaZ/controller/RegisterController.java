package com.AlphaZ.controller;

import com.AlphaZ.constant.SessionConstant;
import com.AlphaZ.constant.StatusCode;
import com.AlphaZ.dao.FriendDAO;
import com.AlphaZ.dao.UserDAO;
import com.AlphaZ.entity.AlphazUserEntity;
import com.AlphaZ.entity.FriendsEntity;
import com.AlphaZ.entity.api.ResponseModel;
import com.AlphaZ.util.valid.ValideHelper;
import com.AlphaZ.viewmodel.UserViewModel;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * ProjectName: IM
 * PackageName: com.AlphaZ.controller
 * User: C0dEr
 * Date: 2017/5/26
 * Time: 下午1:28
 * Description:This is a class of com.AlphaZ.controller
 */
@RestController
@RequestMapping("register")
@Transactional
public class RegisterController {
    @Resource
    UserDAO userDAO;
    @Resource
    FriendDAO friendDAO;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseModel register(@ModelAttribute UserViewModel userViewModel, HttpSession session) {
        ResponseModel model = new ResponseModel();
        model.message = "注册成功";
        AlphazUserEntity userEntity = new AlphazUserEntity();
        if (ValideHelper.isNullOrEmpty(userViewModel.username) || ValideHelper.isNullOrEmpty(userViewModel.password)) {
            return new ResponseModel("注册失败", StatusCode.FAIL, null);
        }
        userEntity.setName(userViewModel.username);
        userEntity.setPassword(userViewModel.password);
        userEntity.setStatus(StatusCode.SUCCESS);
        userEntity.setAvatar(userViewModel.avatar);
        userEntity.setCreatetime(new Timestamp(new Date().getTime()));
        userEntity.setPhone(userViewModel.phone);
        userEntity.setGender(userViewModel.gender);
        FriendsEntity entity = new FriendsEntity();
        entity.setGroup("默认分组");
        entity.setUserid(userViewModel.userid.intValue());
        friendDAO.save(entity);
        this.userDAO.save(userEntity);
        userViewModel.userid = userEntity.getId();
        userViewModel.password = "";
        model.data = userViewModel;
        session.setAttribute(SessionConstant.CURRENTUSER, userViewModel);
        return model;
    }

    @RequestMapping(value = "name", method = RequestMethod.GET)
    public Object hasRegister(String name) {
        List<AlphazUserEntity> alphazUserEntities = this.userDAO.findByField(AlphazUserEntity.class, "name", name);
        return alphazUserEntities.size() == 0;

    }
}
