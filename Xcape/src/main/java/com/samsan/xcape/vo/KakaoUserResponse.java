package com.samsan.xcape.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoUserResponse {
    private String id;
    private UserProfileViewPropertiesResponse properties;
    private UserProfileViewKakaoAccountResponse kakao_account;
}
