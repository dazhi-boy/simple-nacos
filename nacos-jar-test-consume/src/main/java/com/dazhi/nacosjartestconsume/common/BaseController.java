package com.dazhi.nacosjartestconsume.common;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
public class BaseController {
    @Resource
    private RestTemplate restTemplate;

    @GetMapping("hello")
    public String getHello(String name) {

//        String url = "http://localhost:8748/base?";

//        String url = "http://nacos-jar-test/base";

        String url = "http://nacos-jar/base";

        return restTemplate.getForObject(url, String.class);
    }
}
