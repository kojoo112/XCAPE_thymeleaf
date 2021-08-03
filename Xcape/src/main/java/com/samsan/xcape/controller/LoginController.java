package com.samsan.xcape.controller;

import com.samsan.xcape.service.HintService;
import com.samsan.xcape.service.UserService;
import com.samsan.xcape.util.XcapeConstant;
import com.samsan.xcape.vo.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String login(String code, HttpSession session) {
        // 1번 인증코드 요청 전달
        TokenWithUserIdVO tokenWithUserIdVO = userService.getAccessToken(code);
        // 2번 인증코드로 토큰 전달
        UserVO userInfo = userService.getUserInfo(tokenWithUserIdVO);

        if(userInfo.getEmail() != null) {
            session.setAttribute(XcapeConstant.USER_INFO, userInfo);
            session.setAttribute(XcapeConstant.ACCESS_TOKEN, tokenWithUserIdVO.getAccessToken());
        }

        return "redirect:/main";
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpSession session) {
        userService.kakaoLogout((String) session.getAttribute(XcapeConstant.ACCESS_TOKEN));
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/main")
    public String main(HttpSession session, Model model){
        UserVO userInfo = (UserVO) session.getAttribute(XcapeConstant.USER_INFO);

        List<MerchantVO> merchantList = hintService.getMerchantList(userInfo);
        List<ThemeVO> themeList = hintService.getThemeList("MRC001", userInfo);

        model.addAttribute("merchantLists", merchantList);
        model.addAttribute("themeLists", themeList);
        return "main";
    }
}
