package com.samsan.xcape.vo;

import lombok.Data;

@Data
public class UserVO {
    private int seq;
    private String userId;
    private String email;
    private String name;
    private String picture;
    private String creDate;
    private String modDate;
}
