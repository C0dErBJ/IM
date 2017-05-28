package com.AlphaZ.controller;

import com.AlphaZ.constant.SessionConstant;
import com.AlphaZ.dao.FriendDAO;
import com.AlphaZ.dao.MsgDAO;
import com.AlphaZ.dao.UserDAO;
import com.AlphaZ.entity.AlphazUserEntity;
import com.AlphaZ.entity.FriendsEntity;
import com.AlphaZ.entity.MsgEntity;
import com.AlphaZ.entity.api.ResponseModel;
import com.AlphaZ.util.valid.ValideHelper;
import com.AlphaZ.viewmodel.UserViewModel;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ProjectName: IM
 * PackageName: com.AlphaZ.controller
 * User: C0dEr
 * Date: 2017/5/27
 * Time: 下午2:56
 * Description:This is a class of com.AlphaZ.controller
 */
@RestController
@Transactional
@RequestMapping("user")
public class UserController {
    @Resource
    UserDAO userDAO;
    @Resource
    FriendDAO friendDAO;
    @Resource
    MsgDAO msgDAO;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseModel getUser(@SessionAttribute(SessionConstant.CURRENTUSER) UserViewModel userViewModel) {
        if (userViewModel == null) {
            return new ResponseModel("用户未登录", 1, null);
        }
        List<AlphazUserEntity> userEntityList = userDAO.findByField(AlphazUserEntity.class, "id", userViewModel.getUserid());
        UserViewModel user = new UserViewModel();
        user.username = userEntityList.get(0).getName();
        user.avatar = userEntityList.get(0).getAvatar();
        user.gender = userEntityList.get(0).getGender();
        user.phone = userEntityList.get(0).getPhone();
        return new ResponseModel("", 0, user);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public ResponseModel update(@SessionAttribute(SessionConstant.CURRENTUSER) UserViewModel use, UserViewModel userViewModel) {
        if (use == null) {
            return new ResponseModel("用户未登录", 1, null);
        }
        List<AlphazUserEntity> userEntityList = userDAO.findByField(AlphazUserEntity.class, "id", use.getUserid());
        userEntityList.get(0).setName(userViewModel.username);
        userEntityList.get(0).setAvatar(userViewModel.avatar);
        userEntityList.get(0).setGender(userViewModel.gender);
        userEntityList.get(0).setPhone(userViewModel.phone);
        userDAO.createOrUpdate(userEntityList.get(0));
        return new ResponseModel("", 0, null);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseModel getUser(@PathVariable Long id) {
        List<AlphazUserEntity> userEntityList = userDAO.findByField(AlphazUserEntity.class, "id", id);
        if (ValideHelper.isNullOrEmpty(userEntityList)) {
            return new ResponseModel("", 0, new UserViewModel());
        }
        UserViewModel user = new UserViewModel();
        user.username = userEntityList.get(0).getName();
        user.avatar = userEntityList.get(0).getAvatar();
        user.gender = userEntityList.get(0).getGender();
        user.phone = userEntityList.get(0).getPhone();
        user.userid=userEntityList.get(0).getId();
        return new ResponseModel("", 0, user);
    }

    @RequestMapping(value = "friends", method = RequestMethod.GET)
    public ResponseModel getUserfriends(@SessionAttribute(SessionConstant.CURRENTUSER) UserViewModel use) {
        if (use == null) {
            return new ResponseModel("", 1, null);
        }
        List<FriendsEntity> userEntityList = friendDAO.findByField(FriendsEntity.class, "userid", use.getUserid().intValue());
        userEntityList.stream().forEach(a -> {
            if (a.getAvatar() == null) {
                a.setAvatar(11);
            }
        });
        Map<String, Object> fri = new HashMap<>();
        List<String> groups = userEntityList.stream().map(a -> a.getGroup()).distinct().collect(Collectors.toList());
        for (String str : groups) {
            List<FriendsEntity> uses = userEntityList.stream().filter(a -> str.equals(a.getGroup()) && a.getFriendid() != null).collect(Collectors.toList());
            fri.put(str, uses);
        }
        return new ResponseModel("", 0, fri);
    }

    @RequestMapping(value = "group", method = RequestMethod.PUT)
    public ResponseModel setgroup(@SessionAttribute(SessionConstant.CURRENTUSER) UserViewModel use, FriendsEntity group) {
        Map<String, Object> param = new HashMap<>();
        param.put("userid", use.getUserid().intValue());
        param.put("friendid", group.getFriendid());
        List<FriendsEntity> friendsEntities = friendDAO.findByFields(FriendsEntity.class, param);
        if (ValideHelper.isNullOrEmpty(friendsEntities)) {
            group.setUserid(use.userid.intValue());
            friendDAO.createOrUpdate(group);
        } else {
            friendsEntities.get(0).setGroup(group.getGroup());
            friendDAO.createOrUpdate(friendsEntities.get(0));
        }
        return new ResponseModel("", 0, null);
    }

    @RequestMapping(value = "group", method = RequestMethod.POST)
    public ResponseModel creategroup(@SessionAttribute(SessionConstant.CURRENTUSER) UserViewModel use, FriendsEntity group) {
        Map<String, Object> param = new HashMap<>();
        param.put("userid", use.getUserid().intValue());
        param.put("group", group.getGroup());
        List<FriendsEntity> friendsEntities = friendDAO.findByFields(FriendsEntity.class, param);
        if (ValideHelper.isNullOrEmpty(friendsEntities)) {
            group.setUserid(use.userid.intValue());
            friendDAO.createOrUpdate(group);
        } else {
            return new ResponseModel("重复分组", 1, null);
        }
        return new ResponseModel("", 0, null);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public ResponseModel deleteUser(@SessionAttribute(SessionConstant.CURRENTUSER) UserViewModel use, @PathVariable Integer userId) {
        Map<String, Object> param = new HashMap<>();
        param.put("userid", use.getUserid().intValue());
        param.put("friendid", userId);
        List<FriendsEntity> friendsEntity = this.friendDAO.findByFields(FriendsEntity.class, param);
        if (!ValideHelper.isNullOrEmpty(friendsEntity)) {
            this.friendDAO.delete(FriendsEntity.class, friendsEntity.get(0).getId());
        }
        return new ResponseModel("", 0, null);
    }

    @RequestMapping(value = "search", method = RequestMethod.GET)
    public ResponseModel findUser(@SessionAttribute(SessionConstant.CURRENTUSER) UserViewModel use, String keyword) {
        Map<String, Object> param = new HashMap<>();
        param.put("name", "%" + keyword + "%");
        param.put("phone", "%" + keyword + "%");
        param.put("gender", "%" + keyword + "%");
        List<AlphazUserEntity> user = this.userDAO.findLikeFields(AlphazUserEntity.class, param);
        List<FriendsEntity> friend = this.friendDAO.findByField(FriendsEntity.class, "userid", use.getUserid().intValue());
        return new ResponseModel("", 0, user.stream().filter(a -> !friend.stream().anyMatch(b -> b.getFriendid() != null && b.getFriendid() == a.getId().intValue()) && use.userid != a.getId()).collect(Collectors.toList()));
    }

    @RequestMapping(value = "addfriend", method = RequestMethod.POST)
    public ResponseModel addFriend(@SessionAttribute(SessionConstant.CURRENTUSER) UserViewModel use, FriendsEntity group) {

        List<AlphazUserEntity> user = this.userDAO.findByField(AlphazUserEntity.class, "id", group.getFriendid().longValue());
        if (!ValideHelper.isNullOrEmpty(user)) {
            FriendsEntity entity = new FriendsEntity();
            entity.setFriendid(user.get(0).getId().intValue());
            entity.setUserid(use.userid.intValue());
            entity.setGroup(group.getGroup());
            entity.setAvatar(user.get(0).getAvatar() != null ? user.get(0).getAvatar().intValue() : null);
            entity.setUsername(user.get(0).getName());
            FriendsEntity entity1 = new FriendsEntity();
            entity1.setUserid(user.get(0).getId().intValue());
            entity1.setFriendid(use.userid.intValue());
            entity1.setGroup(group.getGroup());
            entity1.setAvatar(user.get(0).getAvatar() != null ? user.get(0).getAvatar().intValue() : null);
            entity1.setUsername(user.get(0).getName());
            this.friendDAO.save(entity);
            this.friendDAO.save(entity1);
        }
        return new ResponseModel("添加成功", 0, user);
    }

    @RequestMapping(value = "requestfriend", method = RequestMethod.POST)
    public ResponseModel requestfriend(@SessionAttribute(SessionConstant.CURRENTUSER) UserViewModel use, MsgEntity friend) {
        friend.setFromwho(use.userid.intValue());
        friend.setIsread(1);
        friend.setUsername(use.username);
        friend.setType("好友请求");
        this.msgDAO.save(friend);
        return new ResponseModel("请求成功", 0, null);
    }

    @RequestMapping(value = "groups", method = RequestMethod.GET)
    public ResponseModel requestfriend(@SessionAttribute(SessionConstant.CURRENTUSER) UserViewModel use) {
        List<FriendsEntity> friendsEntities = this.friendDAO.findByField(FriendsEntity.class, "userid", use.getUserid().intValue());
        List<String> groups = friendsEntities.stream().map(a -> a.getGroup()).distinct().collect(Collectors.toList());
        return new ResponseModel("请求成功", 0, groups);
    }

}
