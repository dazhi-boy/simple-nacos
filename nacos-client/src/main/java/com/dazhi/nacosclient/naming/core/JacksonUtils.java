package com.dazhi.nacosclient.naming.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JacksonUtils {
    static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static ObjectNode createEmptyJsonNode() {
        return new ObjectNode(mapper.getNodeFactory());
    }

    public static ArrayNode createEmptyArrayNode() {
        return new ArrayNode(mapper.getNodeFactory());
    }
}
