package com.samsan.xcape.controller;

import com.samsan.xcape.service.UserService;
import com.samsan.xcape.vo.UserVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
@Log4j2
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public ModelAndView login(String code, HttpSession session) {
        // 1번 인증코드 요청 전달
        String access_token = userService.getAccessToken(code);
        // 2번 인증코드로 토큰 전달
        UserVO userInfo = userService.getUserInfo(access_token);
//        log.info("login info : " + userInfo.toString());
        if(userInfo.getEmail() != null) {
            session.setAttribute("userInfo", userInfo);
            // accessToken도 UserVO??
            session.setAttribute("access_token", access_token);
        }
        ModelAndView modelAndView = new ModelAndView("forward:/main","userInfo", userInfo);
//        modelAndView.addObject("userInfo", userInfo);
//        modelAndView.setViewName("forward:/main");
        return modelAndView;
    }

    @RequestMapping(value = "/logout")
    public ModelAndView logout(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        userService.kakaoLogout((String) session.getAttribute("access_token"));
        session.removeAttribute("accessToken");
        session.removeAttribute("userInfo");
        session.invalidate();
        modelAndView.setViewName("redirect:/index");
        return modelAndView;
    }
}
