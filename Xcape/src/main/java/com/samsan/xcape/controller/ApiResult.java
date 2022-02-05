package com.samsan.xcape.controller;

import com.samsan.xcape.enums.ApiResultEnum;
import com.samsan.xcape.service.CommonResult;
import com.samsan.xcape.service.ReturnCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiResult extends CommonResult {
    private ApiResultEnum apiResultEnum;

    public ApiResult(CommonResult commonResult) {
        super(commonResult.getReturnCode(), commonResult.getReturnMessage(), commonResult.getInfo());
    }

    public ApiResult(ReturnCode returnCode, String returnMessage, Object info, ApiResultEnum apiResultEnum) {
        super(returnCode, returnMessage, info);
        this.apiResultEnum = apiResultEnum;
    }
}
