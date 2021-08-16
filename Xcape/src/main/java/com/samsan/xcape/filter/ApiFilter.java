package com.samsan.xcape.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.samsan.xcape.service.UserService;
import com.samsan.xcape.util.CookieUtil;
import com.samsan.xcape.util.XcapeConstant;
import com.samsan.xcape.vo.HintVO;
import com.samsan.xcape.vo.ThemeVO;
import com.samsan.xcape.vo.UserVO;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@WebFilter(urlPatterns = "/api/*")
public class ApiFilter implements Filter {

    private final UserService userService;

    public ApiFilter(UserService userService) {
        this.userService = userService;
    }

    @SneakyThrows
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        //전처리
        //밑에 두줄은 Content 길이만 생성해주고 밑에 doFilter에서 byteArray에 내용이 들어간다.
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);

        String token = CookieUtil.getCookie((HttpServletRequest) request, XcapeConstant.ACCESS_TOKEN);

        //doFilter이후에 찍어야된다.
        chain.doFilter(requestWrapper, responseWrapper);
        String url = requestWrapper.getRequestURI();

        //후처리
        //req
        HttpSession session = requestWrapper.getSession();

        String reqContent = new String(requestWrapper.getContentAsByteArray());
//        log.info("request status : {}, requestBody : {}", url, reqContent);

        String resContent = new String(responseWrapper.getContentAsByteArray());
        //여기서 내용을 다 빼버리기 떄문에 밑에 copyBodyToResponse() 사용!
        int httpStatus = responseWrapper.getStatus();

        UserVO validateUserInfo = validateUserInfo(token, session);

        switch (url) {
            case XcapeConstant.GET_MERCHANT_LIST:
                log.info(">>>> getMerhcnatList >>>");
                break;
            case XcapeConstant.GET_HINT_LIST:
                log.info(">>>> getHintList >>>");
                break;
            case XcapeConstant.GET_THEME_LIST:
                log.info(">>>> getThemeList >>>");
                break;
            case XcapeConstant.REGISTER_HINT:
                registerHint(reqContent, validateUserInfo);
                break;
            case XcapeConstant.MODIFY_HINT_CODE:
                modifyHintCode(reqContent, validateUserInfo);
                break;
            case XcapeConstant.MODIFY_MESSAGE:
                modifyMessage(reqContent, validateUserInfo);
                break;
            case XcapeConstant.DELETE_HINT:
                deleteHint(reqContent, validateUserInfo);
                break;
            default:
                ((HttpServletResponse) response).sendError(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
//        log.info(resContent);
        responseWrapper.copyBodyToResponse();


        log.info("response status : {}, responseBody : {}", httpStatus, resContent);
    }

    public UserVO validateUserInfo(String token, HttpSession session){
        UserVO userVO = (UserVO) session.getAttribute(XcapeConstant.USER_INFO);
        HttpStatus httpStatus = userService.verifyAccessToken(token);

        if (httpStatus != HttpStatus.OK && httpStatus != HttpStatus.UNAUTHORIZED) {
            // 400 에러인 경우
//            return false;
        } else if (httpStatus == HttpStatus.UNAUTHORIZED) {
            // 401 에러인 경우
            token = userService.renewAccessTokenByRefreshToken(userVO);
        }

        // 카카오의 정보와 세션 userInfo가 같은지 검사
        if (!userService.isKakaoAuthUser(token, userVO)) {
            session.invalidate();
        }

        return userService.findUserByEmail(userVO.getEmail());
    }

    private boolean deleteHint(String reqContent, UserVO validateUserInfo) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        HintVO hintVO = objectMapper.readValue(reqContent, HintVO.class);
        if(!hintVO.getStoreName().equals(validateUserInfo.getStoreName())){
            return false;
        }
        return true;
    }

    private boolean modifyMessage(String reqContent, UserVO validateUserInfo) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HintVO hintVO = objectMapper.readValue(reqContent, HintVO.class);
        if(!hintVO.getStoreName().equals(validateUserInfo.getStoreName())){
            return false;
        }
        return true;
    }

    private boolean modifyHintCode(String reqContent, UserVO validateUserInfo) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HintVO registerHint = objectMapper.readValue(reqContent, HintVO.class);
        if(!registerHint.getStoreName().equals(validateUserInfo.getStoreName())){
            return false;
        }
        return true;
    }

    public boolean registerHint(String reqContent, UserVO validateUserInfo) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        HintVO hintVO = objectMapper.readValue(reqContent, HintVO.class);
        if(!hintVO.getStoreName().equals(validateUserInfo.getStoreName())){
            return false;
        }
        return true;
    }
}
