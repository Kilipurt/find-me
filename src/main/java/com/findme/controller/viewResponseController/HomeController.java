package com.findme.controller.viewResponseController;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

    private Logger logger = Logger.getLogger(HomeController.class);

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String home() {
        logger.info("HomeController home method.");
        return "index";
    }
}
