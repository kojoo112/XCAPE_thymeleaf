package com.samsan.xcape.service;

import com.samsan.xcape.vo.HintVO;
import com.samsan.xcape.vo.UserVO;

public interface HintService {

    CommonResult getHintList(HintVO hintVO);

    CommonResult getMerchantList(UserVO userVO);

    CommonResult registerHint(HintVO hintVO);

    CommonResult getThemeList(String merchantCode);

    int getHintCount(String key);

    CommonResult modifyMessage(HintVO hintVO);

//    void deleteHint(HintVO hintVO);

    CommonResult<Boolean> modifyHintCode(String key, int seq);

    CommonResult deleteHint(int seq);
}
