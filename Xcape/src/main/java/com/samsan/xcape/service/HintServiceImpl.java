package com.samsan.xcape.service;

import com.samsan.xcape.dao.HintDAO;
import com.samsan.xcape.util.RandomKeyValue;
import com.samsan.xcape.vo.HintVO;
import com.samsan.xcape.vo.MerchantVO;
import com.samsan.xcape.vo.ThemeVO;
import com.samsan.xcape.vo.UserVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HintServiceImpl implements HintService{

    private final HintDAO hintDAO;

    HintServiceImpl(HintDAO hintDAO){
        this.hintDAO = hintDAO;
    }

    @Override
    public List<HintVO> getHintList(HintVO hintVO) {
        return hintDAO.getHintList(hintVO);
    }

    @Override
    public List<MerchantVO> getMerchantList(UserVO userVO) {
        return hintDAO.getMerchantList(userVO);
    }

    @Override
    public void registerHint(HintVO hintVO) {
        String randomKeyValue = RandomKeyValue.getRandomKey();
        while(isKeyOverlap(randomKeyValue)){
            randomKeyValue = RandomKeyValue.getRandomKey();
        }
        hintVO.setKey(randomKeyValue);
        hintDAO.registerHint(hintVO);
    }

    @Override
    public List<ThemeVO> getThemeList(String merchantCode) {
        return hintDAO.getThemeList(merchantCode);
    }

    private boolean isKeyOverlap(String key){
        return hintDAO.getHintCount(key) != 0;
    }

    @Override
    public int getHintCount(String key) {
        return hintDAO.getHintCount(key);
    }

    @Override
    public void modifyMessage(HintVO hintVO) {
        hintDAO.modifyMessage(hintVO);
    }

    @Override
    public void deleteHint(HintVO hintVO) {
        hintDAO.deleteHint(hintVO);
    }

    @Override
    public boolean modifyHintCode(String key, int seq) {
        if(!isKeyOverlap(key)) {
            hintDAO.modifyHintCode(key, seq);
            return true;
        } else {
            return false;
        }
    }
}
