package edu.hm.cs.projektstudium.findlunch.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginControllerSWA {

    @RequestMapping(value = "/swa/login", method = RequestMethod.GET)
    public String login() {
        return "swa_login";
    }

    @RequestMapping(value = "/swa")
    public String startPage() {
        return "swa_login";
    }
}
