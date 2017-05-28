package com.AlphaZ.controller;

import com.AlphaZ.constant.SessionConstant;
import com.AlphaZ.dao.NoteDAO;
import com.AlphaZ.entity.NoteEntity;
import com.AlphaZ.entity.api.ResponseModel;
import com.AlphaZ.viewmodel.UserViewModel;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("note")
@Transactional
public class NoteController {
    @Resource
    NoteDAO noteDAO;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseModel sendMsg(NoteEntity note, @SessionAttribute(SessionConstant.CURRENTUSER) UserViewModel user) {
        note.setAvatar(user.avatar.intValue());
        note.setCreatetime(new Timestamp(new Date().getTime()));
        note.setFromwho(user.userid.intValue());
        note.setUsename(user.getUsername());
        this.noteDAO.save(note);
        return new ResponseModel("评价成功", 0, null);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseModel getJJ( @SessionAttribute(SessionConstant.CURRENTUSER) UserViewModel user) {
        List<NoteEntity> notes=this.noteDAO.findByField(NoteEntity.class,"towho",user.getUserid().intValue());
        return new ResponseModel("查询成功", 0, notes);
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseModel geJJ(@PathVariable Integer id) {
        List<NoteEntity> notes=this.noteDAO.findByField(NoteEntity.class,"towho",id);
        return new ResponseModel("查询成功", 0, notes);
    }
}
