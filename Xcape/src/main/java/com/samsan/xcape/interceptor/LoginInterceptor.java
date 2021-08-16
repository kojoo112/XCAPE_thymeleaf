package com.samsan.xcape.interceptor;

import com.samsan.xcape.service.UserService;
import com.samsan.xcape.util.CookieUtil;
import com.samsan.xcape.util.XcapeConstant;
import com.samsan.xcape.vo.RenewAccessTokenVO;
import com.samsan.xcape.vo.UserVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Log4j2
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final UserService userService;

    public LoginInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        UserVO userVO = (UserVO) session.getAttribute(XcapeConstant.USER_INFO);
        String accessToken = CookieUtil.getCookie(request, XcapeConstant.ACCESS_TOKEN);

        if (!ObjectUtils.isEmpty(userVO)) {
            // 현재 AccessToken이 유효한지 검사
            HttpStatus httpStatus = userService.verifyAccessToken(accessToken);

            if (httpStatus != HttpStatus.OK && httpStatus != HttpStatus.UNAUTHORIZED) {
                // 400 에러인 경우
                return false;
            } else if (httpStatus == HttpStatus.UNAUTHORIZED) {
                // 401 에러인 경우
                accessToken = userService.renewAccessTokenByRefreshToken(userVO);
            }

            // 카카오의 정보와 세션 userInfo가 같은지 검사
            if (!userService.isKakaoAuthUser(accessToken, userVO)) {
                response.sendError(HttpStatus.BAD_REQUEST.value());
                return false;
            }

            return true;
        } else {
            response.sendRedirect("/");
            return false;
        }
    }
}
