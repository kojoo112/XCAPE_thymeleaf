package com.samsan.xcape.vo;

import com.nhncorp.lucy.security.xss.XssPreventer;
import com.samsan.xcape.enums.Merchant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HintVO {
    private int seq;
    private String userId;
    private Merchant merchant;
    private String themeCode;
    private String themeName;
    private String key;
    private String message1;
    private String message2;
    private char use;
    private String create_time;

//    public void setMessage2(String message2) {
//        this.message2 = XssPreventer.escape(message2);
//    }

//    public String getMessage2() {
//        return XssPreventer.unescape(message2);
//    }

    public String getMerchantCode(){
        return merchant.getMerchantCode();
    }

    public String getMerchantName(){
        return merchant.getMerchantName();
    }

    public void setMerchant(String merchantCode) {
        this.merchant = Merchant.getMerchantEnum(merchantCode);
    }
}
