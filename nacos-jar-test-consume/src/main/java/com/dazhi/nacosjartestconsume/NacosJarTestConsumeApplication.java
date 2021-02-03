package com.dazhi.nacosjartestconsume;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
public class NacosJarTestConsumeApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosJarTestConsumeApplication.class, args);
    }

}
