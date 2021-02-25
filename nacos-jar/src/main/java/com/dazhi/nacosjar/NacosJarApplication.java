package com.dazhi.nacosjar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class NacosJarApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosJarApplication.class, args);
    }

}
