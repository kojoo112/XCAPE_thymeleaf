package com.samsan.xcape.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.samsan.xcape.dao.UserDAO;
import com.samsan.xcape.vo.UserVO;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
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
                userVO.setUserId(payload.getSubject());
                userVO.setPicture((String) payload.get("picture"));
                userVO.setName((String) payload.get("name"));

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
}
