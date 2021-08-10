package com.samsan.xcape.vo;

import com.nhncorp.lucy.security.xss.XssPreventer;
import com.samsan.xcape.enums.Merchant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HintVO {
    private int seq;
    private String storeName;
//    private Merchant merchant;
    private String themeCode;
    private String themeName;
    private String key;
    private String message1;
    private String message2;
    private char use;
    private String regDate;
    private String modDate;
    private String merchantCode;
    private String merchantName;

//    public String getMerchantCode(){
//        return merchant.getMerchantCode();
//    }
//
//    public String getMerchantName(){
//        return merchant.getMerchantName();
//    }
//
//    public void setMerchant(String merchantCode) {
//        this.merchant = Merchant.getMerchantEnum(merchantCode);
//    }
}
