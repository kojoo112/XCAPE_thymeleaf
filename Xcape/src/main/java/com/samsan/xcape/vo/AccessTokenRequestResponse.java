package com.samsan.xcape.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessTokenRequestResponse {
    private String access_token;
    private String refresh_token;
    private String token_type;
    private Integer expires_in;
    private Integer refresh_token_expires_in;
    private String scope;
}
