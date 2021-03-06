package com.dazhi.nacosjartest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class NacosJarTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosJarTestApplication.class, args);
    }

}
