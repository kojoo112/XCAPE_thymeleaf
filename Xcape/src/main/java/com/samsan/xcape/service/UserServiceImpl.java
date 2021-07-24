package com.samsan.xcape.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.samsan.xcape.dao.UserDAO;
import com.samsan.xcape.vo.UserVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@Log4j2
public class UserServiceImpl implements UserService{

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

    /**
     * Kakao Login
     * @param accessToken
     * @return
     *
     */
    @Override
    public UserVO getUserInfo(String accessToken) {
        String reqUrl = "https://kapi.kakao.com/v2/user/me";
        UserVO userInfo = new UserVO();
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            int responseCode = conn.getResponseCode();
//            log.info("responseCode = " + responseCode);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";
            while((line = bufferedReader.readLine()) != null) {
                result += line;
            }
//            log.info("userInfo response body = " + result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            String nickname = properties.getAsJsonObject().get("nickname").getAsString();
            String email = kakaoAccount.getAsJsonObject().get("email").getAsString();
            String id = element.getAsJsonObject().get("id").getAsString();

            // 가입된 유저라면
            if(getUserCount(email) > 0){
                UserVO existUser = findUserByEmail(email);

                userInfo.setEmail(existUser.getEmail());
                userInfo.setNickname(existUser.getNickname());
                userInfo.setId(existUser.getId());
                userInfo.setCreDate(existUser.getCreDate());
                userInfo.setModDate(existUser.getModDate());
            } else {
                // 신규가입
                userInfo.setNickname(nickname);
                userInfo.setEmail(email);
                userInfo.setId(id);
                signUp(userInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    @Override
    public String getAccessToken(String code) {
        String accessToken = "";
        String refreshToken = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("grant_type=authorization_code");
            stringBuilder.append("&client_id=aa8169c90be18a546cbcbff22067ea51");
            stringBuilder.append("&redirect_uri=http://localhost:8080/login");
            stringBuilder.append("&code=" + code);

            bufferedWriter.write(stringBuilder.toString());
            bufferedWriter.flush();

            int responseCode = conn.getResponseCode();
//            log.info("response code =" + responseCode);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";
            while((line = bufferedReader.readLine()) != null) {
                result += line;
            }
//            log.info("response body = " + result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            accessToken = element.getAsJsonObject().get("access_token").getAsString();
            refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

            bufferedReader.close();
            bufferedWriter.close();
        } catch (Exception e){
            e.printStackTrace();
        }

        return accessToken;
    }

    @Override
    public void kakaoLogout(String accessToken) {
        String reqURL = "http://kapi.kakao.com/v1/user/logout";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            int responseCode = conn.getResponseCode();
//            log.info("responseCode = " + responseCode);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String result = "";
            String line = "";

            while((line = bufferedReader.readLine()) != null){
                result = line;
            }
//            log.info(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
