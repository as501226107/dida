package com.dream.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Scope("prototype")
public class PageController {
    @RequestMapping("/page/{page}")
    public String page(@PathVariable("page") String page){
        return page;
    }
}
