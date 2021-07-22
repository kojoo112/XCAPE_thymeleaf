package com.samsan.xcape.service;

import com.samsan.xcape.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;

public interface UserService {

    public int getUserCount(String email);

    public void signUp(UserVO userVO);

    public String googleLogin(String idtoken, HttpServletRequest request, HttpServletResponse response) throws GeneralSecurityException, IOException;

    public UserVO findUserByEmail(String email);
}
