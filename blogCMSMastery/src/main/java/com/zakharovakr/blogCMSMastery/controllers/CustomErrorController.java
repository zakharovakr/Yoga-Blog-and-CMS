package com.zakharovakr.blogCMSMastery.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;

//not using this
public class CustomErrorController implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public String error() {
        return "customError";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

}
