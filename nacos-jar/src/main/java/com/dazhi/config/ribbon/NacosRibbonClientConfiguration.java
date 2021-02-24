package com.dazhi.config.ribbon;

import com.dazhi.nacosjar.registry.NacosDiscoveryProperties;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ServerList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.ribbon.PropertiesFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnRibbonNacos
public class NacosRibbonClientConfiguration {
    @Autowired
    private PropertiesFactory propertiesFactory;

    @Bean
    @ConditionalOnMissingBean
    public ServerList<?> ribbonServerList(IClientConfig config,
                                          NacosDiscoveryProperties nacosDiscoveryProperties) {
        if (this.propertiesFactory.isSet(ServerList.class, config.getClientName())) {
            ServerList serverList = this.propertiesFactory.get(ServerList.class, config,
                    config.getClientName());
            return serverList;
        }
        NacosServerList serverList = new NacosServerList(nacosDiscoveryProperties);
        serverList.initWithNiwsConfig(config);
        return serverList;
    }


    @Bean
    @ConditionalOnMissingBean
    public NacosServerIntrospector nacosServerIntrospector() {
        return new NacosServerIntrospector();
    }
}
