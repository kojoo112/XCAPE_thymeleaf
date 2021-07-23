package com.samsan.xcape.controller;

import com.samsan.xcape.service.HintService;
import com.samsan.xcape.vo.HintVO;
import com.samsan.xcape.vo.MerchantVO;
import com.samsan.xcape.vo.ThemeVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Log4j2
@Controller
public class MainController {

    private final HintService hintService;

    public MainController(HintService hintService) {
        this.hintService = hintService;
    }

    @GetMapping("index")
    public void index() {

    }

    @GetMapping("/main")
    public String main(Model model) {
        // default value
        List<MerchantVO> merchantList = hintService.getMerchantList();
        List<ThemeVO> themeList = hintService.getThemeList("MRC001");
//        List<HintVO> hintList = hintService.getHint("MRC001", "THM001");
        model.addAttribute("merchantLists", merchantList);
        model.addAttribute("themeLists", themeList);
        return "main";
    }
}
