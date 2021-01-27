package com.dazhi.nacosclient.naming.controller;

import com.dazhi.nacosclient.naming.core.Instance;
import com.dazhi.nacosclient.naming.core.JacksonUtils;
import com.dazhi.nacosclient.naming.core.Service;
import com.dazhi.nacosclient.naming.core.ServiceManager;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.util.VersionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/instance")
public class InstanceController {
    @Autowired
    private ServiceManager serviceManager;

    @GetMapping("/list")
    public ObjectNode list(HttpServletRequest request) throws Exception {
        String namespaceId = optional(request, "namespaceId", "public");
        String serviceName = "DEFAULT_GROUP@@"+request.getParameter("serviceName");

        String clusters = optional(request, "clusters", StringUtils.EMPTY);
        String clientIP = optional(request, "clientIP", StringUtils.EMPTY);

        return doSrvIpxt(namespaceId, serviceName, clusters, clientIP);
    }

    private ObjectNode doSrvIpxt(String namespaceId, String serviceName, String clusters, String clientIP) {
        ObjectNode result = JacksonUtils.createEmptyJsonNode();

        Service service = serviceManager.getService(namespaceId, serviceName);

        List<Instance> srvedIPs;

        srvedIPs = service.srvIPs(Arrays.asList(StringUtils.split(clusters, ",")));


        Map<Boolean, List<Instance>> ipMap = new HashMap<>(2);
        ipMap.put(Boolean.TRUE, new ArrayList<>());
        ipMap.put(Boolean.FALSE, new ArrayList<>());

        for (Instance ip : srvedIPs) {
            ipMap.get(Boolean.TRUE).add(ip);
        }

        ArrayNode hosts = JacksonUtils.createEmptyArrayNode();

        for (Map.Entry<Boolean, List<Instance>> entry : ipMap.entrySet()) {
            List<Instance> ips = entry.getValue();

            for (Instance instance : ips) {

                ObjectNode ipObj = JacksonUtils.createEmptyJsonNode();

                ipObj.put("ip", instance.getIp());
                ipObj.put("port", instance.getPort());
                // deprecated since nacos 1.0.0:
                ipObj.put("valid", entry.getKey());
                ipObj.put("healthy", entry.getKey());
//                ipObj.put("marked", instance.isMarked());
                ipObj.put("instanceId", instance.getInstanceId());
//                ipObj.set("metadata", JacksonUtils.transferToJsonNode(instance.getMetadata()));
//                ipObj.put("enabled", instance.isEnabled());
//                ipObj.put("weight", instance.getWeight());
                ipObj.put("clusterName", instance.getClusterName());
//                if (clientInfo.type == ClientInfo.ClientType.JAVA
//                        && clientInfo.version.compareTo(VersionUtil.parseVersion("1.0.0")) >= 0) {
                    ipObj.put("serviceName", instance.getServiceName());
//                } else {
//                    ipObj.put("serviceName", NamingUtils.getServiceName(instance.getServiceName()));
//                }

//                ipObj.put("ephemeral", instance.isEphemeral());
                hosts.add(ipObj);

            }
        }
        result.replace("hosts", hosts);
            result.put("dom", serviceName);
        result.put("name", serviceName);
//        result.put("cacheMillis", cacheMillis);
        result.put("lastRefTime", System.currentTimeMillis());
//        result.put("checksum", service.getChecksum());
        result.put("useSpecifiedURL", false);
        result.put("clusters", clusters);
//        result.put("env", env);
//        result.replace("metadata", JacksonUtils.transferToJsonNode(service.getMetadata()));
        return result;
    }

    @PostMapping
    public String register(HttpServletRequest request) throws Exception {
        // 获取参数
        final String namespaceId = optional(request, "namespaceId", "public");
        final String serviceName = "DEFAULT_GROUP@@"+request.getParameter("serviceName");

        //将数据封装到一个实例中
        final Instance instance = parseInstance(request);
        serviceManager.registerInstance(namespaceId, serviceName, instance);
        return "ok";
    }

    private Instance parseInstance(HttpServletRequest request) {
        String serviceName = "DEFAULT_GROUP@@"+request.getParameter("serviceName");
        String app = optional(request, "app", "DEFAULT");
        Instance instance = getIpAddress(request);
        instance.setApp(app);
        instance.setServiceName(serviceName);
        instance.setInstanceId(instance.generateInstanceId());
        return instance;
    }

    private Instance getIpAddress(HttpServletRequest request) {
        final String ip = required(request, "ip");
        final String port = required(request, "port");
        String cluster = optional(request, "clusterName", StringUtils.EMPTY);
        if (StringUtils.isBlank(cluster)) {
            cluster = optional(request, "cluster", "DEFAULT");
        }

        Instance instance = new Instance();
        instance.setPort(Integer.parseInt(port));
        instance.setIp(ip);
        instance.setClusterName(cluster);

        return instance;
    }

    public static String required(final HttpServletRequest req, final String key) {
        String value = req.getParameter(key);
        if (StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException("Param '" + key + "' is required.");
        }
        String encoding = req.getParameter("encoding");
        return resolveValue(value, encoding);
    }

    public static String optional(final HttpServletRequest req, final String key, final String defaultValue) {
        if (!req.getParameterMap().containsKey(key) || req.getParameterMap().get(key)[0] == null) {
            return defaultValue;
        }
        String value = req.getParameter(key);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        String encoding = req.getParameter("encoding");
        return resolveValue(value, encoding);
    }

    private static String resolveValue(String value, String encoding) {
        if (StringUtils.isEmpty(encoding)) {
            encoding = StandardCharsets.UTF_8.name();
        }
        try {
            value = new String(value.getBytes(StandardCharsets.UTF_8), encoding);
        } catch (UnsupportedEncodingException ignore) {
        }
        return value.trim();
    }
}
