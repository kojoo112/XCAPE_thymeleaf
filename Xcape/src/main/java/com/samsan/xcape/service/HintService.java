package com.samsan.xcape.service;

import com.samsan.xcape.vo.HintVO;
import com.samsan.xcape.vo.MerchantVO;
import com.samsan.xcape.vo.ThemeVO;
import com.samsan.xcape.vo.XcapeUser;

import java.util.List;

public interface HintService {

    List<HintVO> getHintList(HintVO hintVO);

    List<MerchantVO> getMerchantList(XcapeUser xcapeUser);

    void registerHint(HintVO hintVO);

    List<ThemeVO> getThemeList(String merchantCode);

    int getHintCount(String key);

    void modifyMessage(HintVO hintVO);

    void deleteHint(HintVO hintVO);

    boolean modifyHintCode(String key, int seq);
}
