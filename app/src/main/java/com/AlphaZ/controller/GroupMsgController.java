package com.AlphaZ.controller;

import com.AlphaZ.constant.SessionConstant;
import com.AlphaZ.constant.StatusCode;
import com.AlphaZ.dao.GroupmsgDAO;
import com.AlphaZ.dao.UserDAO;
import com.AlphaZ.entity.GroupmsgEntity;
import com.AlphaZ.entity.api.ResponseModel;
import com.AlphaZ.util.valid.ValideHelper;
import com.AlphaZ.viewmodel.UserViewModel;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ProjectName: IM
 * PackageName: com.AlphaZ.controller
 * User: C0dEr
 * Date: 2017/5/26
 * Time: 下午2:17
 * Description:This is a class of com.AlphaZ.controller
 */
@RestController
@RequestMapping("gmsg")
@Transactional
public class GroupMsgController {

    @Resource
    UserDAO userDAO;
    @Resource
    GroupmsgDAO groupmsgDAO;


    @RequestMapping(value = "getrequest", method = RequestMethod.GET)
    public ResponseModel getRequest(@SessionAttribute(SessionConstant.CURRENTUSER) UserViewModel user) {
        List<GroupmsgEntity> msg = groupmsgDAO.findAll(GroupmsgEntity.class);
        return new ResponseModel("发送成功", StatusCode.SUCCESS, msg.stream().filter(a -> a.getIsread() != 0).collect(Collectors.toList()));
    }

    @RequestMapping(value = "read/{id}", method = RequestMethod.PUT)
    public ResponseModel getRequest(@PathVariable Long id) {
        List<GroupmsgEntity> msg = groupmsgDAO.findByField(GroupmsgEntity.class, "id", id);
        if (!ValideHelper.isNullOrEmpty(msg)) {
            msg.get(0).setIsread(0);
            groupmsgDAO.createOrUpdate(msg.get(0));
        }
        return new ResponseModel("发送成功", StatusCode.SUCCESS, msg);
    }

    @RequestMapping(value = "user", method = RequestMethod.GET)
    public ResponseModel getMsg(@SessionAttribute(SessionConstant.CURRENTUSER) UserViewModel user) {
        List<GroupmsgEntity> msg = groupmsgDAO.findAll(GroupmsgEntity.class);
        Map<String, Object> p = new HashMap<>();
        p.put("current", user.getUserid());
        p.put("chat", msg.stream().sorted(Comparator.comparing(GroupmsgEntity::getCreatetime)).collect(Collectors.toList()));
        return new ResponseModel("发送成功", StatusCode.SUCCESS, p);
    }

    @RequestMapping(value = "new", method = RequestMethod.GET)
    public ResponseModel getNewmsg(@SessionAttribute(SessionConstant.CURRENTUSER) UserViewModel user, String time) {
        List<GroupmsgEntity> msg = groupmsgDAO.findAll(GroupmsgEntity.class);
        Map<String, Object> p = new HashMap<>();
        p.put("current", user.getUserid());
        List<GroupmsgEntity> msgs = msg.stream().filter(a -> a.getIsread() == 1 && a.getHassend() == 1 && a.getFromwho() != user.userid.intValue()).sorted(Comparator.comparing(GroupmsgEntity::getCreatetime)).collect(Collectors.toList());
        p.put("chat", msgs);
        msgs.forEach(a -> a.setHassend(0));
        groupmsgDAO.batchCreateOrUpdate(msgs);
        return new ResponseModel("发送成功", StatusCode.SUCCESS, p);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseModel sendMsg(GroupmsgEntity msg, @SessionAttribute(SessionConstant.CURRENTUSER) UserViewModel user) {
        List<GroupmsgEntity> allmsg = groupmsgDAO.findAll(GroupmsgEntity.class);
        allmsg.forEach(a -> a.setIsread(0));
        groupmsgDAO.batchCreateOrUpdate(allmsg);
        msg.setAvatar(user.getAvatar().intValue());
        msg.setUsername(user.getUsername());
        msg.setFromwho(user.userid.intValue());
        msg.setIsread(1);
        msg.setHassend(1);
        msg.setCreatetime(new Timestamp(new Date().getTime()));
        this.groupmsgDAO.save(msg);
        return new ResponseModel("", 0, msg);
    }

    @RequestMapping(value = "downlload", method = RequestMethod.GET)
    public void downloadmsg(@SessionAttribute(SessionConstant.CURRENTUSER) UserViewModel user, HttpServletResponse response) {
        List<GroupmsgEntity> msg = groupmsgDAO.findAll(GroupmsgEntity.class);
        Map<String, Object> p = new HashMap<>();
        p.put("current", user.getUserid());
        p.put("chat", msg.stream().filter(a -> !a.getMsg().equals("好友请求")).sorted(Comparator.comparing(GroupmsgEntity::getCreatetime)).collect(Collectors.toList()));
        response.setContentType("text/plain");

        try {
            List<GroupmsgEntity> mm = msg.stream().filter(a -> !a.getMsg().equals("好友请求")).sorted(Comparator.comparing(GroupmsgEntity::getCreatetime)).collect(Collectors.toList());

            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            for (int i = 0; i < mm.size(); i++) {
                outputStream.write(("用户：" + mm.get(i).getUsername() + "," + "消息:" + mm.get(i).getMsg()).getBytes());
                outputStream.write("\r\n".getBytes());
            }

            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseModel delete(@PathVariable Long id) {
        this.groupmsgDAO.delete(GroupmsgEntity.class, id);
        return new ResponseModel("", 0, null);
    }
}

