package com.samsan.xcape.service;

import com.samsan.xcape.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;

public interface UserService {

    public int getUserCount(String email);

    public void signUp(UserVO userVO);

    public UserVO findUserByEmail(String email);

    public UserVO getUserInfo(String accessToken);

    public String getAccessToken(String code);

    public void kakaoLogout(String accessToken);
}
