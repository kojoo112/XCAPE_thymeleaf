package com.samsan.xcape.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RenewAccessTokenVO {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private String refresh_token_expires_in;
    private String expires_in;
}
