package com.dazhi.nacosjar.registry;

import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;

public class NacosServiceRegistry implements ServiceRegistry<Registration> {
    public NacosServiceRegistry(){
        System.out.println("--------------------------------------NacosServiceRegistry");
    }

    @Override
    public void register(Registration registration) {
        System.out.println("--------------------------------------register");
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
