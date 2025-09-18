package com.rakesh.sms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/SMSClient")
public class MainController {

    @GetMapping("/")
    public String dashboard() {
        return "SMSClient/index"; // if using templates
    }
}
