package com.dazhi.nacosjar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
//@Import(NacosRibbonClientConfiguration.class)
//@RibbonClient(name="microservice-provider-user",configuration = NacosRibbonClientConfiguration.class)
//@ComponentScan(excludeFilters={@ComponentScan.Filter(type= FilterType.ANNOTATION,value= ConditionalOnRibbonNacos.class)})
public class NacosJarApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosJarApplication.class, args);
    }

}
