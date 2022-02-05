package com.samsan.xcape.service;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommonResult<T> {
    private ReturnCode returnCode = ReturnCode.SUCCESS;
    private String returnMessage;
    private T info;

    public void setOk(String returnMessage, T info) {
        setReturnCode(ReturnCode.SUCCESS);
        setReturnMessage(returnMessage);
        setInfo(info);
    }

    public void setError(Throwable throwable, T info) {
        setReturnCode(ReturnCode.FAIL);
        setReturnMessage(throwable.getMessage());
        setInfo(info);
    }

}
