package com.findme.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeControllerViewResponse {

    private Logger logger = Logger.getLogger(HomeControllerViewResponse.class);

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String home() {
        logger.info("HomeController home method.");
        return "index";
    }
}
