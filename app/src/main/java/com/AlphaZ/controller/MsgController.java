package com.AlphaZ.controller;

import com.AlphaZ.constant.SessionConstant;
import com.AlphaZ.constant.StatusCode;
import com.AlphaZ.dao.MsgDAO;
import com.AlphaZ.dao.UserDAO;
import com.AlphaZ.entity.MsgEntity;
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
@RequestMapping("msg")
@Transactional
public class MsgController {

    @Resource
    UserDAO userDAO;
    @Resource
    MsgDAO msgDAO;


    @RequestMapping(value = "getrequest", method = RequestMethod.GET)
    public ResponseModel getRequest(@SessionAttribute(SessionConstant.CURRENTUSER) UserViewModel user) {
        List<MsgEntity> msg = msgDAO.findByField(MsgEntity.class, "towho", user.getUserid().intValue());
        return new ResponseModel("发送成功", StatusCode.SUCCESS, msg.stream().filter(a -> a.getIsread() != 0).collect(Collectors.toList()));
    }

    @RequestMapping(value = "read/{id}", method = RequestMethod.PUT)
    public ResponseModel getRequest(@PathVariable Long id) {
        List<MsgEntity> msg = msgDAO.findByField(MsgEntity.class, "id", id);
        if (!ValideHelper.isNullOrEmpty(msg)) {
            msg.get(0).setIsread(0);
            msgDAO.createOrUpdate(msg.get(0));
        }
        return new ResponseModel("发送成功", StatusCode.SUCCESS, msg);
    }

    @RequestMapping(value = "user/{id}", method = RequestMethod.GET)
    public ResponseModel getMsg(@SessionAttribute(SessionConstant.CURRENTUSER) UserViewModel user, @PathVariable Integer id) {
        Map<String, Object> param = new HashMap<>();
        param.put("fromwho", id);
        param.put("towho", user.getUserid().intValue());
        List<MsgEntity> msg = msgDAO.findByFields(MsgEntity.class, param);
        param.put("fromwho", user.getUserid().intValue());
        param.put("towho", id);
        msg.addAll(msgDAO.findByFields(MsgEntity.class, param));
        Map<String, Object> p = new HashMap<>();
        p.put("current", user.getUserid());
        p.put("chat", msg.stream().filter(a -> !a.getMsg().equals("好友请求")).sorted(Comparator.comparing(MsgEntity::getCreatetime)).collect(Collectors.toList()));
        return new ResponseModel("发送成功", StatusCode.SUCCESS, p);
    }

    @RequestMapping(value = "new/{id}", method = RequestMethod.GET)
    public ResponseModel getNewmsg(@SessionAttribute(SessionConstant.CURRENTUSER) UserViewModel user, @PathVariable Integer id, String time) {
        Map<String, Object> param = new HashMap<>();
        param.put("fromwho", id);
        param.put("towho", user.getUserid().intValue());
        List<MsgEntity> msg = msgDAO.findByFields(MsgEntity.class, param);
        Map<String, Object> p = new HashMap<>();
        p.put("current", user.getUserid());
        Timestamp ti = Timestamp.valueOf(time);
        p.put("chat", msg.stream().filter(a -> !a.getMsg().equals("好友请求") && a.getIsread() == 1 && a.getCreatetime().after(ti)).sorted(Comparator.comparing(MsgEntity::getCreatetime)).collect(Collectors.toList()));
        return new ResponseModel("发送成功", StatusCode.SUCCESS, p);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseModel sendMsg(MsgEntity msg, @SessionAttribute(SessionConstant.CURRENTUSER) UserViewModel user) {
        msg.setType("聊天");
        msg.setAvatar(user.getAvatar().intValue());
        msg.setUsername(user.getUsername());
        msg.setFromwho(user.userid.intValue());
        msg.setIsread(1);
        msg.setCreatetime(new Timestamp(new Date().getTime()));
        this.msgDAO.save(msg);
        Map<String, Object> param = new HashMap<>();
        param.put("fromwho", msg.getTowho());
        param.put("towho", user.getUserid().intValue());
        List<MsgEntity> allmsg = msgDAO.findByFields(MsgEntity.class, param);
        param.put("fromwho", user.getUserid().intValue());
        param.put("towho", msg.getTowho());
        allmsg.addAll(msgDAO.findByFields(MsgEntity.class, param));
        allmsg.forEach(a -> a.setIsread(0));
        msgDAO.batchCreateOrUpdate(allmsg);
        return new ResponseModel("", 0, msg);
    }

    @RequestMapping(value = "downlload/{id}", method = RequestMethod.GET)
    public void downloadmsg(@SessionAttribute(SessionConstant.CURRENTUSER) UserViewModel user, @PathVariable Integer id, HttpServletResponse response) {
        Map<String, Object> param = new HashMap<>();
        param.put("fromwho", id);
        param.put("towho", user.getUserid().intValue());
        List<MsgEntity> msg = msgDAO.findByFields(MsgEntity.class, param);
        param.put("fromwho", user.getUserid().intValue());
        param.put("towho", id);
        msg.addAll(msgDAO.findByFields(MsgEntity.class, param));
        Map<String, Object> p = new HashMap<>();
        p.put("current", user.getUserid());
        p.put("chat", msg.stream().filter(a -> !a.getMsg().equals("好友请求")).sorted(Comparator.comparing(MsgEntity::getCreatetime)).collect(Collectors.toList()));
        response.setContentType("text/plain");

        try {
            List<MsgEntity> mm = msg.stream().filter(a -> !a.getMsg().equals("好友请求")).sorted(Comparator.comparing(MsgEntity::getCreatetime)).collect(Collectors.toList());

            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            for (int i = 0; i < mm.size(); i++) {
                outputStream.write(("用户：" + mm.get(i).getUsername() + "," + "消息:" + mm.get(0).getMsg()).getBytes());
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
        this.msgDAO.delete(MsgEntity.class, id);
        return new ResponseModel("", 0, null);
    }
}

