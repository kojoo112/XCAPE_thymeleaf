package com.samsan.xcape.controller;

import com.samsan.xcape.service.HintService;
import com.samsan.xcape.vo.HintVO;
import com.samsan.xcape.vo.MerchantVO;
import com.samsan.xcape.vo.ThemeVO;
import com.samsan.xcape.vo.UserVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/api")
public class HintController {

    private final HintService hintService;

    public HintController(HintService hintService) {
        this.hintService = hintService;
    }

    @GetMapping("/theme/list")
    @ResponseBody
    public List<ThemeVO> getThemeList(String merchantCode){
        List<ThemeVO> themeVOList = hintService.getThemeList(merchantCode);
        return themeVOList;
    }

    @GetMapping("/merchant/list")
    @ResponseBody
    public List<MerchantVO> getMerchantList(UserVO userVO){
        List<MerchantVO> merchantVOList = hintService.getMerchantList(userVO);
        return merchantVOList;
    }

    @GetMapping("/getHintList")
    @ResponseBody
    public List<HintVO> getHintList(HintVO hintVO){
        List<HintVO> hintList = hintService.getHintList(hintVO);
        return hintList;
    }

    @PostMapping("/registerHint")
    public void registerHint(@RequestBody HintVO hintVO){
        hintService.registerHint(hintVO);
    }

    @PostMapping("/modifyMessage")
    public void modifyMessage(@RequestBody HintVO hintVO){
        hintService.modifyMessage(hintVO);
    }

    @PostMapping("/deleteHint")
    public void deleteHint(@RequestBody HintVO hintVO){
        hintService.deleteHint(hintVO);
    }

    @PostMapping("/modifyHintCode")
    public ResponseEntity modifyHintCode(String key, int seq){
        HttpStatus httpStatus;

        boolean isModified = hintService.modifyHintCode(key, seq);
        if(isModified) {
            httpStatus = HttpStatus.OK;
        } else {
            httpStatus = HttpStatus.ACCEPTED;
        }
        return new ResponseEntity(httpStatus);
    }
}
