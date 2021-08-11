package com.samsan.xcape.vo;

import com.samsan.xcape.enums.Merchant;
import lombok.Data;

@Data
public class ThemeVO {
    private int seq;
    private String storeName;
    private Merchant merchant;
    private String themeCode;
    private String themeName;
    private char use;
    private String regDate;
    private String modDate;
}
