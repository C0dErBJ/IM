package com.AlphaZ.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * ProjectName: AlphaZ
 * PackageName: com.AlphaZ.handler
 * User: C0dEr
 * Date: 2017/4/21
 * Time: 下午2:25
 * Description:This is a class of com.AlphaZ.handler
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    public static final String DEFAULT_ERROR_VIEW = "system/500";

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName(DEFAULT_ERROR_VIEW);
        return mav;
    }
}
