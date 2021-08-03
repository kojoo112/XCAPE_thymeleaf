package com.samsan.xcape.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenWithUserIdVO {
    private String accessToken;
    private String refreshToken;
    private String id;
}
