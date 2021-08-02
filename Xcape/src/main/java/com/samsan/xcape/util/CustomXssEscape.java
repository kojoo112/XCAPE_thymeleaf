package com.samsan.xcape.util;

import com.nhncorp.lucy.security.xss.XssPreventer;
import com.samsan.xcape.vo.HintVO;

public class CustomXssEscape {

    public static HintVO SetEscapeMessage(HintVO hintVO) {
        hintVO.setMessage1(XssPreventer.escape(hintVO.getMessage1()));
        hintVO.setMessage2(XssPreventer.escape(hintVO.getMessage2()));
        return hintVO;
    }
}
