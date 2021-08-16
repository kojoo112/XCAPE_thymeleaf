package com.samsan.xcape.service;

import com.samsan.xcape.vo.TokenWithUserIdVO;
import com.samsan.xcape.vo.XcapeUser;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public interface UserService {

    int getUserCount(String email);

    XcapeUser signUp(XcapeUser xcapeUser);

    XcapeUser findUserByEmail(String email);

    XcapeUser getUserInfo(TokenWithUserIdVO tokenVO);

    TokenWithUserIdVO getAccessToken(String code);

    void kakaoLogout(String accessToken);

    void registRefreshToken(TokenWithUserIdVO refreshToken);

    HttpStatus verifyAccessToken(String accessToken);

    String renewAccessTokenByRefreshToken(XcapeUser xcapeUser);

    boolean isKakaoAuthUser(String accessToken, XcapeUser sessionUser);

    Optional<XcapeUser> findByEmail(String email);
}
