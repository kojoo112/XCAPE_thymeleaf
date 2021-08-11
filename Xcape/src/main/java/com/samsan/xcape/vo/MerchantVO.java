package com.samsan.xcape.vo;

import com.samsan.xcape.enums.Merchant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MerchantVO {
    private int seq;
    private String storeName;
    private Merchant merchant;
    private String regDate;
    private String modDate;

    public void setMerchant(String merchantCode) {
        this.merchant = Merchant.getMerchantEnum(merchantCode);
    }
}
