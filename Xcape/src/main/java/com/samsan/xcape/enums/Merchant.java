package com.samsan.xcape.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.Arrays;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public enum Merchant {
    MRC001("MRC001", "강남-엑스케이프"),
    MRC002("MRC002", "건대-엑스케이프"),
    MRC003("MRC003", "건대-엑스크라임"),
    MRC004("MRC004", "수원-엑스케이프");

    String merchantCode;
    String merchantName;

    Merchant(String merchantCode, String merchantName) {
        this.merchantCode = merchantCode;
        this.merchantName = merchantName;
    }

    Merchant(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public static Merchant getMerchantEnum(String merchantCode) {
        for (Merchant merchant : values()) {
            if (merchant.getMerchantCode().equalsIgnoreCase(merchantCode)) {
                return merchant;
            }
        }
        return null;
    }

    public static boolean isMerchantCode(String merchantCode) {
        return Arrays.stream(values())
                .filter(merchant ->
                        merchant.merchantCode.equals(merchantCode))
                .findAny()
                .isPresent();

//                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 가맹점 코드입니다."));
    }

}
