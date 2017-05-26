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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
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

    @RequestMapping(value = "sidebar", method = RequestMethod.GET)
    public ModelAndView sidebar(HttpSession session) {
        ModelAndView mav = new ModelAndView("common/sidebar");
        if (session.getAttribute(SessionConstant.CURRENTUSER) != null) {
            UserViewModel uvm = (UserViewModel) session.getAttribute(SessionConstant.CURRENTUSER);
            mav.addObject("name", uvm.name);
            mav.addObject("role", uvm.role.values().stream().collect(Collectors.joining(",")));
            mav.addObject("avatar", uvm.avatar == 0 ? "resources/img/default.jpg" : "file/pic/" + uvm.avatar);
            try {
                if (ValideHelper.isNullOrEmpty(uvm.menuKV)) {
                    mav.addObject("auth", "{'htm':'nil'}");
                } else {
                    mav.addObject("auth", new ObjectMapper().writeValueAsString(uvm.menuKV));
                }

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else {
            mav.addObject("name", "未能正常显示");
            mav.addObject("role", "未能正常显示");
            mav.addObject("avatar", "resources/img/default.jpg");
            mav.addObject("auth", "{'htm':'nil'}");
        }
        return mav;
    }

    @RequestMapping(value = "sidebarmenu", method = RequestMethod.GET)
    @ResponseBody
    public ResponseModel menu(HttpSession session) {
        ResponseModel model = new ResponseModel("查询失败", StatusCode.FAIL, null);
        if (session.getAttribute(SessionConstant.CURRENTUSER) != null) {
            UserViewModel uvm = (UserViewModel) session.getAttribute(SessionConstant.CURRENTUSER);
            model = userService.getMenuByUserId(uvm.userid);
        }
        return model;
    }

    @RequestMapping(value = "head", method = RequestMethod.GET)
    public ModelAndView head(HttpSession session) {
        ModelAndView mav = new ModelAndView("common/head");
        if (session.getAttribute(SessionConstant.CURRENTUSER) != null) {
            UserViewModel uvm = (UserViewModel) session.getAttribute(SessionConstant.CURRENTUSER);
            mav.addObject("name", uvm.name);
            mav.addObject("role", uvm.role.values().stream().collect(Collectors.joining(",")));
            mav.addObject("avatar", uvm.avatar == 0 ? "resources/img/default.jpg" : "file/pic/" + uvm.avatar);
        } else {
            mav.addObject("name", "未能正常显示");
            mav.addObject("role", "未能正常显示");
            mav.addObject("avatar", "resources/img/default.jpg");
        }
        return mav;
    }

    @RequestMapping(value = "layout", method = RequestMethod.GET)
    public ModelAndView layout() {
        return new ModelAndView("common/layout");
    }

    @RequestMapping(value = "foot", method = RequestMethod.GET)
    public ModelAndView foot() {
        return new ModelAndView("common/foot");
    }

    @RequestMapping(value = "role", method = RequestMethod.GET)
    public ModelAndView role() {
        return new ModelAndView("role");
    }

    @RequestMapping(value = "towelcome.htm", method = RequestMethod.GET)
    public ModelAndView welcome() {
        return new ModelAndView("welcome");
    }

    @RequestMapping(value = "touserlist.htm", method = RequestMethod.GET)
    public ModelAndView toUserList() {
        return new ModelAndView("userlist");
    }

    @RequestMapping(value = "touserinfo.htm", method = RequestMethod.GET)
    public ModelAndView toUserInfo() {
        return new ModelAndView("userinfo");
    }

    @RequestMapping(value = "touseraddskip.htm", method = RequestMethod.GET)
    public ModelAndView toUserAddSkip(HttpSession session, String pagemsg) {
        session.setAttribute(SessionConstant.SESSION_PAGEMSG, pagemsg);
        ModelAndView mav = new ModelAndView("useradd");
        if (session.getAttribute(SessionConstant.CURRENTUSER) != null) {
            UserViewModel uvm = (UserViewModel) session.getAttribute(SessionConstant.CURRENTUSER);
            mav.addObject("system", userService.isSystemRole(uvm.userid));
        } else {
            mav.addObject("system", false);
        }
        return mav;
    }

    @RequestMapping(value = "/touserinfoskip.htm", method = RequestMethod.GET)
    public String toUserInfoSkip(HttpSession session, Long id, String pagemsg) {
        session.setAttribute(SessionConstant.SESSION_PAGEMSG, pagemsg);
        UserViewModel uvm = (UserViewModel) session.getAttribute(SessionConstant.CURRENTUSER);
        session.setAttribute("hrole", uvm.role.values().stream().collect(Collectors.joining(",")));
        session.setAttribute("id", id);
        return "userinfo";
    }

    @RequestMapping("/toauth.htm")
    public ModelAndView auth() {
        return new ModelAndView("auth");
    }

    @RequestMapping(value = "/touserupdateskip.htm", method = RequestMethod.GET)
    public String toUserUpdateSkip(HttpSession session, Long id, String pagemsg) {
        session.setAttribute(SessionConstant.SESSION_PAGEMSG, pagemsg);
        session.setAttribute("id", id);
        return "userupdate";
    }

    @RequestMapping(value = "/touserupdatepwdskip.htm", method = RequestMethod.GET)
    public String toUserUpdatePwdSkip(HttpSession session, Long id, String pagemsg) {
        session.setAttribute(SessionConstant.SESSION_PAGEMSG, pagemsg);
        session.setAttribute("id", id);
        return "userupdatepwd";
    }


    @RequestMapping(value = "tologs.htm", method = RequestMethod.GET)
    public ModelAndView logs() {
        return new ModelAndView("logs");
    }


    @RequestMapping(value = "/500.htm", method = RequestMethod.GET)
    public ModelAndView error() {
        return new ModelAndView("/system/500");
    }

    @RequestMapping(value = "/404.htm", method = RequestMethod.GET)
    public ModelAndView notFound() {
        return new ModelAndView("/system/404");
    }

    @RequestMapping(value = "/401.htm", method = RequestMethod.GET)
    public ModelAndView notLogin() {
        return new ModelAndView("/system/401");
    }

    @RequestMapping(value = "/noauth.htm", method = RequestMethod.GET)
    public ModelAndView notAuth() {
        return new ModelAndView("/system/notauth");
    }


}
