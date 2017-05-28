package com.AlphaZ.controller;

import com.AlphaZ.constant.SessionConstant;
import com.AlphaZ.constant.StatusCode;
import com.AlphaZ.entity.api.ResponseModel;
import com.AlphaZ.service.UserService;
import com.AlphaZ.util.valid.ValideHelper;
import com.AlphaZ.viewmodel.UserViewModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.stream.Collectors;

/**
 * Created by C0dEr on 2016/12/3.
 */
@RequestMapping("")
@Controller
public class IndexController {
    @Resource
    UserService userService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request) {
        return new ModelAndView("redirect:login");
    }


    @RequestMapping(value = "welcome", method = RequestMethod.GET)
    public ModelAndView welcome(@SessionAttribute(SessionConstant.CURRENTUSER) UserViewModel uvm) {
        ModelAndView mav = new ModelAndView("welcome");
        if (uvm != null) {
            mav.addObject("name", uvm.username);
            mav.addObject("state", uvm.state);
            mav.addObject("avatar", uvm.avatar == 0 ? "resources/img/default.jpg" : "file/" + uvm.avatar);
        } else {
            mav.addObject("name", "未能正常显示");
            mav.addObject("state", "离线");
            mav.addObject("avatar", "resources/img/default.jpg");
        }
        return mav;
    }

    @RequestMapping(value = "register", method = RequestMethod.GET)
    public ModelAndView register() {
        return new ModelAndView("register");
    }

}
