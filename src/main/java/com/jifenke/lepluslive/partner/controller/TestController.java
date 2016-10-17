package com.jifenke.lepluslive.partner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by wcg on 2016/10/17.
 */
@Controller
@RequestMapping("/api")
public class TestController {

    @RequestMapping(value = "/partner/go/test")
    public String goTest(Model model
    ) {
        model.addAttribute("sid", "123");
        return "hbDownload/index";
    }


}
