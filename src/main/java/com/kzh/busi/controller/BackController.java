package com.kzh.busi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/busi")
public class BackController {
    @RequestMapping(value = "/home")
    public String home(Model model) {
        return "busi/home";
    }
}
