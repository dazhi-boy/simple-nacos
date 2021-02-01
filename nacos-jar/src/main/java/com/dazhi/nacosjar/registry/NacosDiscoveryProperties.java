package com.dazhi.nacosjar.registry;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.cloud.nacos.discovery")
public class NacosDiscoveryProperties {


    @Value("${spring.cloud.nacos.discovery.service:${spring.application.name:}}")
    private String service;

    public String getService() {
        return service;
    }
}
