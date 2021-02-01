package com.dazhi.nacosjar.registry;

import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.util.StringUtils;

public class NacosServiceRegistry implements ServiceRegistry<Registration> {
    public NacosServiceRegistry(){
        System.out.println("--------------------------------------NacosServiceRegistry");
    }

    @Override
    public void register(Registration registration) {
        System.out.println("--------------------------------------register");
//        if (StringUtils.isEmpty(registration.getServiceId())) {
//        } else {
//            String serviceId = registration.getServiceId();
//        String serviceId = "nacos-jar";
//                String group = this.nacosDiscoveryProperties.getGroup();
//            Instance instance = this.getNacosInstanceFromRegistration(registration);
//
//            try {
//                this.namingService.registerInstance(serviceId, group, instance);
//                log.info("nacos registry, {} {} {}:{} register finished", new Object[]{group, serviceId, instance.getIp(), instance.getPort()});
//            } catch (Exception var6) {
//                log.error("nacos registry, {} register failed...{},", new Object[]{serviceId, registration.toString(), var6});
//                ReflectionUtils.rethrowRuntimeException(var6);
//            }

//        }
    }

    @Override
    public void deregister(Registration registration) {
        System.out.println("--------------------------------------deregister");
    }

    @Override
    public void close() {
        System.out.println("--------------------------------------close");
    }

    @Override
    public void setStatus(Registration registration, String status) {
        System.out.println("--------------------------------------setStatus");
    }

    @Override
    public <T> T getStatus(Registration registration) {
        System.out.println("--------------------------------------getStatus");
        return null;
    }
}
