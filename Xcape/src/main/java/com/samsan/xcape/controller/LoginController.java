package com.samsan.xcape.controller;

import com.samsan.xcape.service.HintService;
import com.samsan.xcape.service.UserService;
import com.samsan.xcape.vo.MerchantVO;
import com.samsan.xcape.vo.ThemeVO;
import com.samsan.xcape.vo.UserVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@Log4j2
public class LoginController {

    private UserService userService;

    private HintService hintService;

    public LoginController(UserService userService, HintService hintService) {
        this.userService = userService;
        this.hintService = hintService;
    }

    @GetMapping("/login")
    public ModelAndView login(String code, HttpSession session, Model model) {
        // 1번 인증코드 요청 전달
        String access_token = userService.getAccessToken(code);
        // 2번 인증코드로 토큰 전달
        UserVO userInfo = userService.getUserInfo(access_token);
//        log.info("login info : " + userInfo.toString());
        if(userInfo.getEmail() != null) {
            session.setAttribute("userInfo", userInfo);
            // accessToken도 UserVO??
            session.setAttribute("access_token", access_token);
            model.addAttribute("userInfo", userInfo);   // 추가된 코드
        }
        ModelAndView modelAndView = new ModelAndView("main");
        List<MerchantVO> merchantList = hintService.getMerchantList(userInfo);
        List<ThemeVO> themeList = hintService.getThemeList("MRC001", userInfo);
        model.addAttribute("merchantLists", merchantList);
        model.addAttribute("themeLists", themeList);
        return modelAndView;
    }

    @RequestMapping(value = "/logout")
    public ModelAndView logout(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        userService.kakaoLogout((String) session.getAttribute("access_token"));
        session.removeAttribute("accessToken");
        session.removeAttribute("userInfo");
        session.invalidate();
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }
}
