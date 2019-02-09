package com.findme.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String home() {
        return "index";
    }

//    @RequestMapping(path = "/test-ajax", method = RequestMethod.GET)
//    public ResponseEntity<String> testAjax() {
//        return new ResponseEntity<>("trouble", HttpStatus.NOT_FOUND);
//    }
}
