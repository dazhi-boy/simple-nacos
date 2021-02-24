package com.dazhi.nacosjar.registry;

import com.dazhi.nacosjar.common.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.util.ReflectionUtils.rethrowRuntimeException;

public class NacosServiceRegistry implements ServiceRegistry<Registration> {
    private static final Logger log = LoggerFactory.getLogger(NacosServiceRegistry.class);
    private final NacosDiscoveryProperties nacosDiscoveryProperties;

    private final NamingService namingService;

    public NacosServiceRegistry(NacosDiscoveryProperties nacosDiscoveryProperties) {
        System.out.println("--------------------------------------NacosServiceRegistry");
        this.nacosDiscoveryProperties = nacosDiscoveryProperties;
        this.namingService = nacosDiscoveryProperties.namingServiceInstance();
    }

    @Override
    public void register(Registration registration) {
        System.out.println("--------------------------------------register");
        if (StringUtils.isEmpty(registration.getServiceId())) {
            log.warn("No service to register for nacos client...");
            return;
        }

        String serviceId = registration.getServiceId();
        String group = nacosDiscoveryProperties.getGroup();

        //这里发http请求将服务注册到service
        NamingProxy namingProxy = new NamingProxy();
        Map<String, String> params = new HashMap();
        params.put("serviceName", "nacos.naming.serviceName");
        params.put("ip", "20.18.7.10");
        params.put("port", "8080");
        String res = namingProxy.callServer("/instance", params, "", "http://127.0.0.1:8080", HttpMethod.POST);
        System.out.println(res);
        /*Instance instance = getNacosInstanceFromRegistration(registration);

        try {
            namingService.registerInstance(serviceId, group, instance);
            log.info("nacos registry, {} {} {}:{} register finished", group, serviceId,
                    instance.getIp(), instance.getPort());
        }
        catch (Exception e) {
            log.error("nacos registry, {} register failed...{},", serviceId,
                    registration.toString(), e);
            // rethrow a RuntimeException if the registration is failed.
            // issue : https://github.com/alibaba/spring-cloud-alibaba/issues/1132
            rethrowRuntimeException(e);
        }*/
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
        System.out.println("--------------------------------------setStatus-------这里将nacos上面的实例全部缓存下来");
    }

    @Override
    public <T> T getStatus(Registration registration) {
        System.out.println("--------------------------------------getStatus");
        return null;
    }

    private Instance getNacosInstanceFromRegistration(Registration registration) {
        Instance instance = new Instance();
        instance.setIp(registration.getHost());
        instance.setPort(registration.getPort());
//        instance.setWeight(nacosDiscoveryProperties.getWeight());
        instance.setClusterName(nacosDiscoveryProperties.getClusterName());
//        instance.setMetadata(registration.getMetadata());

        return instance;
    }
}
