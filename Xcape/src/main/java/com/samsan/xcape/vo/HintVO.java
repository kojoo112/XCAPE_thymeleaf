package com.samsan.xcape.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.samsan.xcape.enums.Merchant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HintVO {
    private int seq;
    private String companyName;
    private Merchant merchant;
    private String themeCode;
    private String themeName;
    private String key;
    private String message1;
    private String message2;
    private char use;
    private String regDate;
    private String modDate;

//    public String getMerchantCode(){
//        return merchant.getMerchantCode();
//    }
//
//    public String getMerchantName(){
//        return merchant.getMerchantName();
//    }

//    @JsonAnyGetter
//    public Merchant getMerchant() {
//        return merchant;
//    }

    public void setMerchant(String merchantCode) {
        this.merchant = Merchant.getMerchantEnum(merchantCode);
    }
}
