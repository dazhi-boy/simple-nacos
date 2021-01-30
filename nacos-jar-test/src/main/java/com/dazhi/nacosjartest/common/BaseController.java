package com.dazhi.nacosjartest.common;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("base")
public class BaseController {

    @GetMapping
    public String sayHello(){
        return "hello";
    }
}
