package com.samsan.xcape.service;

import com.samsan.xcape.enums.RegularFormatEnum;
import lombok.Getter;

@Getter
public enum ReturnCode implements RegularFormatEnum {
    SUCCESS("0000", "success"),
    FAIL("1001", "fail"),
    RETRY("1002", "retry");


    private final String typeCode;
    private final String description;

    ReturnCode(String typeCode, String description) {
        this.typeCode = typeCode;
        this.description = description;
    }

    public boolean getBooleanResult() {
        return this.equals(SUCCESS);
    }
}
