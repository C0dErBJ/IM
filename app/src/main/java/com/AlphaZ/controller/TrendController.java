package com.AlphaZ.controller;

import com.AlphaZ.constant.SessionConstant;
import com.AlphaZ.dao.TrendDAO;
import com.AlphaZ.entity.TrendEntity;
import com.AlphaZ.entity.api.ResponseModel;
import com.AlphaZ.viewmodel.UserViewModel;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * ProjectName: IM
 * PackageName: com.AlphaZ.controller
 * User: C0dEr
 * Date: 2017/5/28
 * Time: 下午7:47
 * Description:This is a class of com.AlphaZ.controller
 */
@RestController
@RequestMapping("trend")
@Transactional
public class TrendController {
    @Resource
    TrendDAO trendDAO;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseModel sendMsg(TrendEntity note, @SessionAttribute(SessionConstant.CURRENTUSER) UserViewModel user) {
        note.setAvatar(user.avatar.intValue());
        note.setCreatetime(new Timestamp(new Date().getTime()));
        note.setFromwho(user.userid.intValue());
        note.setUsername(user.getUsername());
        this.trendDAO.save(note);
        return new ResponseModel("发布成功", 0, null);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseModel getJJ(@SessionAttribute(SessionConstant.CURRENTUSER) UserViewModel user) {
        List<TrendEntity> notes = this.trendDAO.findAll(TrendEntity.class);
        return new ResponseModel("查询成功", 0, notes);
    }
}
