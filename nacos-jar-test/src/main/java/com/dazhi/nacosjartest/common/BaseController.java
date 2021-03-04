package com.dazhi.nacosjartest.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("base")
public class BaseController {

    @Value("${useLocalCache:false}")
    private boolean useLocalCache;

    @GetMapping
    public String sayHello(){
        return "hello";
    }

    @RequestMapping("/get")
    public boolean get() {
        return useLocalCache;
    }
}
