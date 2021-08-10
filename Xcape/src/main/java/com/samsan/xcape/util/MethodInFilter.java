//package com.samsan.xcape.util;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.samsan.xcape.service.UserService;
//import com.samsan.xcape.vo.HintVO;
//import com.samsan.xcape.vo.UserVO;
//import org.springframework.http.HttpStatus;
//
//import java.util.List;
//
//public class MethodInFilter {
//
//    private final UserService userService;
//
//    public MethodInFilter(UserService userService) {
//        this.userService = userService;
//    }
//
//    /**
//     *
//     * @param token
//     * @param sessionUser
//     * @return
//     */
//    public UserVO validateUserInfo(String token, UserVO sessionUser){
//        HttpStatus httpStatus = userService.verifyAccessToken(token);
//
//        if (httpStatus != HttpStatus.OK && httpStatus != HttpStatus.UNAUTHORIZED) {
//            // 400 에러인 경우
////            return false;
//        } else if (httpStatus == HttpStatus.UNAUTHORIZED) {
//            // 401 에러인 경우
//            token = userService.renewAccessTokenByRefreshToken(sessionUser.getId(), sessionUser.getRefreshToken());
//        }
//
//        // 카카오의 정보와 세션 userInfo가 같은지 검사
//        if (!userService.isKakaoAuthUser(token, sessionUser)) {
////            response.sendError(HttpStatus.BAD_REQUEST.value());
////            return false;
//        }
//
//        return userService.findUserByEmail(sessionUser.getEmail());
//    }
//
//    public void getHintList(String content, UserVO userVO) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        List<HintVO> hintVOLists = objectMapper.readValue(content, new TypeReference<List<HintVO>>() {});
//        for(HintVO hintVO : hintVOLists){
//            if(hintVO.getStoreName().equals(userVO.getStoreName())){
//                // 에러처리
//            }
//        }
//    }
//
//}
