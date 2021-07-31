package com.samsan.xcape.vo;

import lombok.Data;

@Data
public class UserVO {
    private int seq;
    private String id;
    private String role;
    private String nickname;
    private String email;
    private String creDate;
    private String modDate;
}
