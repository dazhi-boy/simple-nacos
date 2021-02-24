package com.dazhi.nacosjar.demo;

import com.dazhi.nacosjar.registry.NacosDiscoveryProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("base")
public class BaseController {

//    @Resource
//    public NacosDiscoveryProperties nacosDiscoveryProperties;

    @GetMapping
    public String sayHello(){
        return "hello";
    }
}
