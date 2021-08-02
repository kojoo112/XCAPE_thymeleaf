package com.samsan.xcape.interceptor;

import com.samsan.xcape.util.XcapeConstant;
import com.samsan.xcape.vo.UserVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Log4j2
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        UserVO userVO = (UserVO) session.getAttribute(XcapeConstant.USER_INFO);
        if(ObjectUtils.isEmpty(userVO)){
            response.sendRedirect("/");
            return false;
        } else {
            return true;
        }
    }
}
