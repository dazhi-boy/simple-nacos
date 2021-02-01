package com.dazhi.nacosjar.registry;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(
        proxyBeanMethods = false
)
public class NacosServiceRegistryAutoConfiguration {
    public NacosServiceRegistryAutoConfiguration() {
    }

    @Bean
    public NacosServiceRegistry nacosServiceRegistry() {
        return new NacosServiceRegistry();
    }

    @Bean
    @ConditionalOnBean(AutoServiceRegistrationProperties.class)
    public NacosRegistration nacosRegistration(
            ApplicationContext context) {
        return new NacosRegistration(context);
    }

    @Bean
    @ConditionalOnBean(AutoServiceRegistrationProperties.class)
    public NacosAutoServiceRegistration nacosAutoServiceRegistration(NacosServiceRegistry registry,
                                                                     AutoServiceRegistrationProperties autoServiceRegistrationProperties,
                                                                     NacosRegistration registration) {
        return new NacosAutoServiceRegistration(registry, autoServiceRegistrationProperties, registration);
    }
}
