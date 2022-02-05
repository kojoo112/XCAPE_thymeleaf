package com.samsan.xcape.service;

import com.samsan.xcape.dao.HintDAO;
import com.samsan.xcape.enums.Merchant;
import com.samsan.xcape.util.RandomKeyValue;
import com.samsan.xcape.vo.HintVO;
import com.samsan.xcape.vo.MerchantVO;
import com.samsan.xcape.vo.ThemeVO;
import com.samsan.xcape.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class HintServiceImpl implements HintService {

    private final HintDAO hintDAO;

    HintServiceImpl(HintDAO hintDAO) {
        this.hintDAO = hintDAO;
    }

    @Override
    public CommonResult getHintList(HintVO hintVO) {
        log.info(">>> HintServiceImpl.getHintList >");
        CommonResult result = new CommonResult();

        if (!Merchant.isMerchantCode(hintVO.getMerchant().getMerchantCode())) {
            result.setError(new IllegalArgumentException("잘못된 가맹점 코드입니다."), hintVO);
        }

        try {
            List<HintVO> hintList = hintDAO.getHintList(hintVO);
            result.setOk("success get hint list", hintList);
        } catch (Exception e) {
            log.error(">>> HintServiceImpl.getHintList error {}", e);
            result.setError(e, hintVO);
        }

        return result;
    }

    @Override
    public CommonResult getMerchantList(UserVO userVO) {
        log.info(">>> HintServiceImpl.getMerchantList > ");
        CommonResult result = new CommonResult();

        try {
            List<MerchantVO> merchantList = hintDAO.getMerchantList(userVO);
            result.setOk("success get merchant list", merchantList);
        } catch (Exception e) {
            log.warn(">>> HintServiceImpl.getThemeList > Exception {}", e);
            result.setError(e, userVO);
        }

        return result;
    }

    @Override
    public CommonResult registerHint(HintVO hintVO) {
        log.info(">>> HintServiceImpl.registerHint > ");
        CommonResult result = new CommonResult();
        String randomKeyValue = RandomKeyValue.getRandomKey();
        while (isKeyOverlap(randomKeyValue)) {
            randomKeyValue = RandomKeyValue.getRandomKey();
        }
        hintVO.setKey(randomKeyValue);

        try {
            hintDAO.registerHint(hintVO);
            result.setOk("success register hint", null);
        } catch (Exception e) {
            result.setError(e, hintVO);
        }

        return result;
    }

    @Override
    public CommonResult getThemeList(String merchantCode) {
        log.info(">>> HintServiceImpl.getThemeList > ");
        CommonResult result = new CommonResult();

        if (!Merchant.isMerchantCode(merchantCode)) {
            result.setError(new IllegalArgumentException("잘못된 가맹점 코드입니다."), merchantCode);
            return result;
        }

        try {
            List<ThemeVO> themeList = hintDAO.getThemeList(merchantCode);
            result.setOk("success get theme list", themeList);
        } catch (IllegalArgumentException e) {
            log.warn(">>> HintServiceImpl.getThemeList > IllegalArgumentException {}", e);
            result.setError(e, merchantCode);
        } catch (Exception e) {
            log.warn(">>> HintServiceImpl.getThemeList > Exception {}", e);
            result.setError(e, merchantCode);
        }

        return result;
    }

    private boolean isKeyOverlap(String key) {
        log.info(">>> HintServiceImpl.isKeyOverlap > {}", key);
        return hintDAO.getHintCount(key) != 0;
    }

    @Override
    public int getHintCount(String key) {
        log.info(">>> HintServiceImpl.getHintCount > {}", key);

        return hintDAO.getHintCount(key);
    }

    @Override
    public CommonResult modifyMessage(HintVO hintVO) {
        log.info(">>> HintServiceImpl.modifyMessage > {}", hintVO);
        CommonResult result = new CommonResult();
        try {
            hintDAO.modifyMessage(hintVO);
            result.setOk("success modify hint message", null);
        } catch (Exception e) {
            result.setError(e, null);
        }

        return result;
    }

    @Override
    public CommonResult<Boolean> modifyHintCode(String key, int seq) {
        log.info(">>> HintServiceImpl.modifyHintCode > {}, {}", key, seq);
        CommonResult result = new CommonResult();
        if (!isKeyOverlap(key)) {
            try {
                hintDAO.modifyHintCode(key, seq);
                result.setOk("success modify hint code", null);
            } catch (Exception e) {
                result.setError(e, null);
            }
            return result;
        }
        result.setError(ReturnCode.RETRY, new IllegalArgumentException("이미 존재하는 힌트코드 입니다."), null);

        return result;
    }

    @Override
    public CommonResult deleteHint(int seq) {
        log.info(">>> HintServiceImpl.deleteHint > {}", seq);
        CommonResult result = new CommonResult<>();

        try {
            hintDAO.deleteHint(seq);
            result.setOk("success delete hint", seq);
        } catch (QueryTimeoutException e) {
            log.warn(">>> HintServiceImpl.deleteHint > QueryTimeoutException {}", seq, e);
            result.setError(e, seq);
        } catch (Exception e) {
            log.error(">>> HintServiceImpl.deleteHint > error {}", seq, e);
            result.setError(e, seq);
        }

        return result;
    }

}
