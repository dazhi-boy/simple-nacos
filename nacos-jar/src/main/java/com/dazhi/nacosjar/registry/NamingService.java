package com.dazhi.nacosjar.registry;

public interface NamingService {

    void registerInstance(String serviceName, String groupName, Instance instance);
}
