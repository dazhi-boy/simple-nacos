package com.dazhi.nacosjar.registry;

import org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;

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
        return null;
    }

    @Override
    protected Registration getManagementRegistration() {
        System.out.println("+++++++++++++++++++++++++++++++++++++getManagementRegistration");
        return null;
    }
}
