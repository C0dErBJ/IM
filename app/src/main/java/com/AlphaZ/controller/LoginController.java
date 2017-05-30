package com.AlphaZ.controller;

import com.AlphaZ.constant.SessionConstant;
import com.AlphaZ.constant.StatusCode;
import com.AlphaZ.dao.UserDAO;
import com.AlphaZ.entity.AlphazUserEntity;
import com.AlphaZ.entity.api.ResponseModel;
import com.AlphaZ.service.UserService;
import com.AlphaZ.viewmodel.UserViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;

/**
 * Created by C0dEr on 2016/12/3.
 */
@RequestMapping("/login")
@Controller
@Transactional
public class LoginController {
    @Resource
    UserService userService;

    @Resource
    UserDAO userDAO;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request, HttpSession session) {
        if (session.getAttribute(SessionConstant.CURRENTUSER) != null) {
            session.removeAttribute(SessionConstant.CURRENTUSER);
        }
        Cookie[] cookies = request.getCookies();
        ModelAndView mav = new ModelAndView("login");
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("a&p")) {
                    String deap = new String(Base64.getDecoder().decode(c.getValue()));
                    String[] ap = deap.split("\\|%\\|#\\|");
                    mav.addObject("account", ap.length >= 1 ? ap[0] : "");
                    mav.addObject("password", ap.length >= 2 ? ap[1] : "");
                    //mav.addObject("rmb", ap.length >= 2);
                    break;
                }
            }
        }
        return mav;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseModel login(String name, String password, Boolean remember, HttpSession session, HttpServletResponse response) {
        if (remember) {
            String namepassword = name.concat("|%|#|").concat(password);
            String namepassword64 = null;
            try {
                namepassword64 = Base64.getEncoder().encodeToString(namepassword.getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Cookie cookie = new Cookie("a&p", namepassword64);
            cookie.setPath("/");
            cookie.setMaxAge(1000 * 60 * 60 * 24 * 30);
            response.addCookie(cookie);
        }
        if (!remember) {
            Cookie cookie = new Cookie("a&p", null);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        ResponseModel model = userService.login(name, password);
        if (model.statusCode == StatusCode.SUCCESS) {
            session.setAttribute(SessionConstant.CURRENTUSER, model.data);
        }
        return model;
    }

    @RequestMapping(value = "/off", method = RequestMethod.GET)
    public ModelAndView logout(HttpSession session, @SessionAttribute(SessionConstant.CURRENTUSER) UserViewModel userViewModel) {
        if (userViewModel != null) {
            List<AlphazUserEntity> userEntityList = userDAO.findByField(AlphazUserEntity.class, "id", userViewModel.getUserid());
            if (userEntityList.size() > 0) {
                userEntityList.get(0).setState(StatusCode.FAIL);
                userDAO.createOrUpdate(userEntityList.get(0));
            }
        }
        session.removeAttribute(SessionConstant.CURRENTUSER);
        return new ModelAndView("redirect:/");
    }


}