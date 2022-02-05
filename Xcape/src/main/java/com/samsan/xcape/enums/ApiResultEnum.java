package com.samsan.xcape.enums;

import lombok.Getter;

@Getter
public enum ApiResultEnum implements RegularFormatEnum {
    SUCCESS("0000", "success"),
    FAIL("1001", "fail"),
    RETRY("1002", "retry");

    private final String typeCode;
    private final String description;

    ApiResultEnum(String typeCode, String description) {
        this.typeCode = typeCode;
        this.description = description;
    }
}
