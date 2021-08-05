package com.samsan.xcape.interceptor;

import com.samsan.xcape.service.UserService;
import com.samsan.xcape.util.XcapeConstant;
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
        String token = (String) session.getAttribute(XcapeConstant.ACCESS_TOKEN);

        if(!ObjectUtils.isEmpty(userVO)){
            HttpStatus httpStatus = userService.verifyAccessToken(token);
            if(httpStatus != HttpStatus.OK && httpStatus == HttpStatus.UNAUTHORIZED){
                token = userService.renewAccessTokenByRefreshToken(userVO.getId(), userVO.getRefreshToken());
            }
            String responseUserId = userService.isKakaoAuthUser(token);
            if(responseUserId != userVO.getId()){
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
