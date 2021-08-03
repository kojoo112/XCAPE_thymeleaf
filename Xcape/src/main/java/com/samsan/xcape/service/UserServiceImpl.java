package com.samsan.xcape.service;

import com.samsan.xcape.dao.UserDAO;
import com.samsan.xcape.vo.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
@Log4j2
public class UserServiceImpl implements UserService{

    @Value("${kakao.user.api.url}")
    private String kakaoUserApiUrl;

    @Value("${kakao.token.api.url}")
    private String kakaoTokenApiUrl;

    @Value("${kakao.logout.api.url}")
    private String kakaoLogoutApiUrl;

    private final UserDAO userDAO;

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public int getUserCount(String email) {
        return userDAO.getUserCount(email);
    }

    @Override
    public void signUp(UserVO userVO) {
        userDAO.signUp(userVO);
    }

    @Override
    public UserVO findUserByEmail(String email) {
        return userDAO.findUserByEmail(email);
    }

    @Override
    public TokenWithUserIdVO getAccessToken(String code) {
        try {
            MultiValueMap<String, Object> mmap = new LinkedMultiValueMap<String, Object>();

            mmap.add("grant_type", "authorization_code"); //필수 고정값
            mmap.add("client_id", "aa8169c90be18a546cbcbff22067ea51"); //카카오 rest_key
            mmap.add("redirect_url", "http://localhost:8080/login"); //응답받은 리턴URL
            mmap.add("code", code); //카카오 로그인 후

            HttpHeaders headers = new HttpHeaders();

            headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=utf-8"); //헤더지정
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String,Object>>(mmap, headers);

            RestTemplate restTemplate = new RestTemplate();
            FormHttpMessageConverter converter = new FormHttpMessageConverter();
            converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_FORM_URLENCODED));
            restTemplate.getMessageConverters().add(converter);

            //code를 이용해 로그인 사용자 token값 가져오기
            ResponseEntity<AccessTokenRequestResponse> tokenResponse = restTemplate.postForEntity(kakaoTokenApiUrl, httpEntity, AccessTokenRequestResponse.class);
            TokenWithUserIdVO tokenVO = new TokenWithUserIdVO();
            tokenVO.setAccessToken(tokenResponse.getBody().getAccess_token());
            tokenVO.setRefreshToken(tokenResponse.getBody().getRefresh_token());
            return tokenVO;
        } catch (Exception e){
            log.info(">>> UserServiceImpl.getAccessToken >>", e);
        }

        return null;
    }

    /**
     * Kakao Login
     * @param tokenWithUserIdVO
     * @return
     *
     */
    @Override
    public UserVO getUserInfo(TokenWithUserIdVO tokenWithUserIdVO) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(tokenWithUserIdVO.getAccessToken());

            HttpEntity httpEntity = new HttpEntity(headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<KakaoUserResponse> result = restTemplate.postForEntity(kakaoUserApiUrl, httpEntity, KakaoUserResponse.class);
            KakaoUserResponse kakaoUserResponse = result.getBody();

            String getKakaoUserEmail = kakaoUserResponse.getKakao_account().getEmail();
            String getKakaoUserNickname = kakaoUserResponse.getProperties().getNickname();
            String getKakaoUserId = kakaoUserResponse.getId();
            String getRefreshToken = tokenWithUserIdVO.getRefreshToken();
            tokenWithUserIdVO.setId(getKakaoUserId);

            // 가입된 유저라면
            if(getUserCount(getKakaoUserEmail) > 0){
                registRefreshToken(tokenWithUserIdVO);
                return findUserByEmail(getKakaoUserEmail);
            } else {
                // 신규가입
                UserVO userInfo = UserVO.builder()
                        .nickname(getKakaoUserNickname)
                        .email(getKakaoUserEmail)
                        .id(getKakaoUserId)
                        .refreshToken(getRefreshToken)
                        .build();

                signUp(userInfo);
                return userInfo;
            }
        } catch (Exception e) {
            log.info(">>> UserServiceImpl.getUserInfo >>", e);
        }
        return null;
    }

    @Override
    public void kakaoLogout(String accessToken) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);

            HttpEntity httpEntity = new HttpEntity(headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<KakaoLogoutResponse> result = restTemplate.postForEntity(kakaoLogoutApiUrl, httpEntity, KakaoLogoutResponse.class);
//            KakaoLogoutResponse kakaoLogoutResponse = result.getBody();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registRefreshToken(TokenWithUserIdVO tokenVO) {
        userDAO.registRefreshToken(tokenVO);
    }
}
