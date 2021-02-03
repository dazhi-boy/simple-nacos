package com.dazhi.nacosjar.registry;

import org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.util.Assert;

/**
 * 这个方法继承AbstractAutoServiceRegistration，里面实现ApplicationListener<WebServerInitializedEvent>，监听WebServerInitializedEvent事件，
 */
public class NacosAutoServiceRegistration extends AbstractAutoServiceRegistration<Registration> {
    private NacosRegistration registration;

    public NacosAutoServiceRegistration(ServiceRegistry<Registration> serviceRegistry, AutoServiceRegistrationProperties autoServiceRegistrationProperties, NacosRegistration registration) {
        super(serviceRegistry, autoServiceRegistrationProperties);
        this.registration = registration;
    }

    /** @deprecated */
    @Deprecated
    public void setPort(int port) {
        this.getPort().set(port);
    }

    @Override
    protected Object getConfiguration() {
        System.out.println("+++++++++++++++++++++++++++++++++++++getConfiguration");
        return null;
    }

    @Override
    protected boolean isEnabled() {
        System.out.println("+++++++++++++++++++++++++++++++++++++isEnabled");
        return true;
    }

    @Override
    protected Registration getRegistration() {
        System.out.println("+++++++++++++++++++++++++++++++++++++getRegistration");
        if (this.registration.getPort() < 0 && this.getPort().get() > 0) {
            this.registration.setPort(this.getPort().get());
        }
        Assert.isTrue(this.registration.getPort() > 0, "service.port has not been set");
        return this.registration;
    }

    @Override
    protected Registration getManagementRegistration() {
        System.out.println("+++++++++++++++++++++++++++++++++++++getManagementRegistration");
        return null;
    }
}
