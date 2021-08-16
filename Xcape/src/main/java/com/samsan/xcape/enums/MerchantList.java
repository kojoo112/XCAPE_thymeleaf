package com.samsan.xcape.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum MerchantList {
    MRC001("강남-엑스케이프"),
    MRC002("건대-엑스케이프"),
    MRC003("건대-엑스크라임"),
    MRC004("수원-엑스케이프");

    private String merchantName;
}
