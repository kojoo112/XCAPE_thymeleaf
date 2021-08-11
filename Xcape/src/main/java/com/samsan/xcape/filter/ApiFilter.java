package com.samsan.xcape.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        UserVO userVO = (UserVO) session.getAttribute(XcapeConstant.USER_INFO);

        String reqContent = new String(requestWrapper.getContentAsByteArray());
        log.info("request status : {}, requestBody : {}", url, reqContent);

        String resContent = new String(responseWrapper.getContentAsByteArray());
        //여기서 내용을 다 빼버리기 떄문에 밑에 copyBodyToResponse() 사용!
        int httpStatus = responseWrapper.getStatus();

        UserVO validateUserInfo = validateUserInfo(token, userVO);

        switch (url) {
            case XcapeConstant.GET_THEME_LIST:
//                getThemeList(resContent, validateUserInfo);
                log.info("apiThemeList");
                break;
            case XcapeConstant.GET_HINT_LIST:
//                getHintList(resContent, validateUserInfo);
                log.info("apiHintList");
                break;
            case XcapeConstant.REGISTER_HINT:
//                registerHint(reqContent, validateUserInfo);
                break;
            case XcapeConstant.MODIFY_HINT_CODE:
//                modifyHintCode(reqContent, validateUserInfo, session);
                break;
            default:
                log.info("예외");
        }

        log.info(resContent);
        responseWrapper.copyBodyToResponse();

        log.info("response status : {}, responseBody : {}", httpStatus, resContent);
    }

    private void getThemeList(String resContent, UserVO validateUserInfo) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<ThemeVO> themeVOList = objectMapper.readValue(resContent, new TypeReference<List<ThemeVO>>() {});
        for(ThemeVO themeVO : themeVOList){
            if(!themeVO.getStoreName().equals(validateUserInfo.getStoreName())){
                // 에러처리
            }
        }
    }

    private void modifyHintCode(String reqContent, UserVO validateUserInfo, HttpSession session) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        HintVO registerHint = objectMapper.readValue(reqContent, HintVO.class);
        if(!registerHint.getStoreName().equals(validateUserInfo.getStoreName())){
            log.info("같지않음.");
        }

        session.invalidate();
    }

    public UserVO validateUserInfo(String token, UserVO sessionUser){
        HttpStatus httpStatus = userService.verifyAccessToken(token);

        if (httpStatus != HttpStatus.OK && httpStatus != HttpStatus.UNAUTHORIZED) {
            // 400 에러인 경우
//            return false;
        } else if (httpStatus == HttpStatus.UNAUTHORIZED) {
            // 401 에러인 경우
            token = userService.renewAccessTokenByRefreshToken(sessionUser.getId(), sessionUser.getRefreshToken());
        }

        // 카카오의 정보와 세션 userInfo가 같은지 검사
        if (!userService.isKakaoAuthUser(token, sessionUser)) {
//            response.sendError(HttpStatus.BAD_REQUEST.value());
//            return false;
        }

        return userService.findUserByEmail(sessionUser.getEmail());
    }

    public void getHintList(String resContent, UserVO validateUserInfo) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
//        HintVO[] hintVOLists = objectMapper.readValue(resContent, HintVO[].class);
        List<HintVO> hintVOLists = objectMapper.readValue(resContent, objectMapper.getTypeFactory().constructCollectionType(List.class, HintVO.class));
//        List<HintVO> hintVOLists = objectMapper.readValue(resContent, new TypeReference<List<HintVO >>(){});
        for(HintVO hintVO : hintVOLists){
            if(!hintVO.getStoreName().equals(validateUserInfo.getStoreName())){
                log.info(">>>>>>>>>>>>>>>>>>>>>>> 같지않음.");
            }
        }
    }

    public void registerHint(String content, UserVO userVO) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
//        String result = objectMapper.writeValue(content, userVO.getStoreName());
//        log.info(result);
    }

}
