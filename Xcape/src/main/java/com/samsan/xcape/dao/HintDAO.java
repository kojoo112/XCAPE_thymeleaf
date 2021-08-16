package com.samsan.xcape.dao;

import com.samsan.xcape.vo.HintVO;
import com.samsan.xcape.vo.MerchantVO;
import com.samsan.xcape.vo.ThemeVO;
import com.samsan.xcape.vo.XcapeUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface HintDAO {

    List<HintVO> getHintList(HintVO hintVO);

    List<MerchantVO> getMerchantList(XcapeUser xcapeUser);

    void registerHint(HintVO hintVO);

    List<ThemeVO> getThemeList(String merchantCode);

    int getHintCount(String key);

    void modifyMessage(HintVO hintVO);

    void deleteHint(HintVO hintVO);

    void modifyHintCode(String key, int seq);
}
