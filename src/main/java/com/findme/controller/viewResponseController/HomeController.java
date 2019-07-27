package com.findme.controller.viewResponseController;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Log4j
public class HomeController {

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String home() {
        log.info("HomeController home method.");
        return "index";
    }
}
