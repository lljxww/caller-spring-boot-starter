package cn.liangjw.apicaller.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author liangjw
 * @version 1.0
 * Create at 008 12/08 16:42
 */
public class JsonNodeUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static List<String> getKeySet(JsonNode jsonNode) {
        if (jsonNode == null) {
            return null;
        }

        List<String> keySet = new ArrayList<>();
        Iterator<Map.Entry<String, JsonNode>> keyValuePairs = jsonNode.fields();

        while (keyValuePairs.hasNext()) {
            Map.Entry<String, JsonNode> keyValuePair = keyValuePairs.next();
            keySet.add(keyValuePair.getKey());
        }

        return keySet;
    }

    public static <T> T getValueWithCaseInsensitiveKey(JsonNode jsonNode, String key, Class<T> clazz) {
        List<String> keySet = getKeySet(jsonNode);
        if (keySet == null) {
            return null;
        }

        String targetKey = null;
        for (String k : keySet) {
            if (key.equalsIgnoreCase(k)) {
                targetKey = k;
                break;
            }
        }

        if (targetKey == null) {
            return null;
        }

        try {
            return MAPPER.treeToValue(jsonNode.get(targetKey), clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
