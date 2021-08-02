package com.samsan.xcape.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {
    private int seq;
    private String id;
    private String role;
    private String nickname;
    private String email;
    private String creDate;
    private String modDate;
}
