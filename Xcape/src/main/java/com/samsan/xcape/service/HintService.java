package com.samsan.xcape.service;

import com.samsan.xcape.vo.HintVO;
import com.samsan.xcape.vo.MerchantVO;
import com.samsan.xcape.vo.ThemeVO;
import com.samsan.xcape.vo.UserVO;

import java.util.List;

public interface HintService {

    List<HintVO> getHintList(String merchantCode, String themeCode);

    List<MerchantVO> getMerchantList(UserVO userVO);

    void registerHint(HintVO hintVO);

    void updateHint(HintVO hintVO);

    List<ThemeVO> getThemeList(String merchantCode, String storeName);

    int getHintCount(String key);

    void modifyMessage(HintVO hintVO);

    void deleteHint(HintVO hintVO);

    boolean modifyHintCode(String key, int seq);
}
