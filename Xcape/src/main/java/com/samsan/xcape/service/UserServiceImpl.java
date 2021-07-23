package com.samsan.xcape.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.samsan.xcape.dao.UserDAO;
import com.samsan.xcape.vo.UserVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
@Log4j2
public class UserServiceImpl implements UserService{

    private final UserDAO userDAO;

    private final String CLIENT_ID = "164344653512-o9cmcj2g320u721tg6qktj66fq6om77e.apps.googleusercontent.com";

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
    public String googleLogin(String idtoken, HttpServletRequest request, HttpServletResponse response) throws GeneralSecurityException, IOException {
        HttpTransport transport = Utils.getDefaultTransport();
        JsonFactory jsonFactory = Utils.getDefaultJsonFactory();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        ObjectMapper mapper = new ObjectMapper();
        GoogleIdToken idToken = verifier.verify(idtoken);
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            int count = getUserCount(payload.getEmail());
            if(count == 0){
                UserVO userVO = new UserVO();
                userVO.setEmail(payload.getEmail());

                HttpSession session = request.getSession();
                session.setAttribute("user", userVO);
                signUp(userVO);
                String result = mapper.writeValueAsString(userVO);
                return result;
            }
            UserVO userVO = findUserByEmail(payload.getEmail());
            HttpSession session = request.getSession();
            session.setAttribute("user", userVO);
            String result = mapper.writeValueAsString(userVO);

            return result;
        } else {
            return "fail";
        }
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
            log.info("responseCode = " + responseCode);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";
            while((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            log.info("userInfo response body = " + result);

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
            log.info("response code =" + responseCode);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";
            while((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            log.info("response body = " + result);

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
            log.info("responseCode = " + responseCode);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String result = "";
            String line = "";

            while((line = bufferedReader.readLine()) != null){
                result = line;
            }
            log.info(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
