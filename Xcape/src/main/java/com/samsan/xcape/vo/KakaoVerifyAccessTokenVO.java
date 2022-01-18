package com.samsan.xcape.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoVerifyAccessTokenVO {
    private String id;
    private String expires_in;
    private String app_id;
}
