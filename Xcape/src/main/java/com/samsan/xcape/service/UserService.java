package com.samsan.xcape.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.samsan.xcape.vo.TokenWithUserIdVO;
import com.samsan.xcape.vo.UserVO;
import org.springframework.http.HttpStatus;

public interface UserService {

    public int getUserCount(String email);

    public void signUp(UserVO userVO);

    public UserVO findUserByEmail(String email);

    public UserVO getUserInfo(TokenWithUserIdVO tokenVO);

    public TokenWithUserIdVO getAccessToken(String code);

    public void kakaoLogout(String accessToken);

    public void registRefreshToken(TokenWithUserIdVO refreshToken);

    public HttpStatus verifyAccessToken(String accessToken);

    public String renewAccessTokenByRefreshToken(String userId, String refreshToken);

    public boolean isKakaoAuthUser(String accessToken, UserVO sessionUser);

}
