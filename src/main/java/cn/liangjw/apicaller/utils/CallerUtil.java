package cn.liangjw.apicaller.utils;

import cn.liangjw.apicaller.CallerContext;
import cn.liangjw.apicaller.properties.ApiItem;
import cn.liangjw.apicaller.properties.CallerProperties;
import cn.liangjw.apicaller.properties.ServiceItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class CallerUtil {
    private final static ObjectMapper mapper = new ObjectMapper();

    public static <T> T firstOrDefault(List<T> source, Predicate<T> predicate) {
        if (source == null) {
            return null;
        }

        Optional<T> result = source.stream().filter(predicate).findFirst();

        if (result == null) {
            return null;
        }

        return result.get();
    }

    public static String[] splitEndpointName(String method) {
        String[] nameArray = method.split("\\.");
        return nameArray;
    }

    public static ServiceItem getServiceItem(CallerProperties callerProperties, String serviceName) {
        ServiceItem serviceItem = CallerUtil.firstOrDefault(callerProperties.getServiceItems(),
                i -> i.getApiName().equalsIgnoreCase(serviceName));
        return serviceItem;
    }

    public static ApiItem getApiItem(ServiceItem serviceItem, String apiName) {
        ApiItem apiItem = CallerUtil.firstOrDefault(serviceItem.getApiItems(),
                i -> i.getMethod().equalsIgnoreCase(apiName));
        return apiItem;
    }

    public static String getFinalUrl(String paramType, String url, @Nullable Object param) {
        JsonNode jsonNode = mapper.convertValue(param, JsonNode.class);

        if ("query".equalsIgnoreCase(paramType)) {
            if (param == null) {
                return url;
            }

            String query = "?";

            Iterator<Map.Entry<String, JsonNode>> keyValuePairs = jsonNode.fields();

            while (keyValuePairs.hasNext()) {
                Map.Entry<String, JsonNode> keyValuePair = keyValuePairs.next();
                query += ("&" + keyValuePair.getKey() + "=" + URLEncoder.encode(keyValuePair.getValue().asText()));
            }

            String finalUrl = url + query;

            if (url.contains("?")) {
                finalUrl = finalUrl.replace("?&", "&");
            } else {
                finalUrl = finalUrl.replace("?&", "?");
            }

            return finalUrl;
        }

        if ("path".equalsIgnoreCase(paramType)) {
            if (param == null) {
                return url;
            }

            String finalUrl = url;

            Iterator<Map.Entry<String, JsonNode>> keyValuePairs = jsonNode.fields();

            while (keyValuePairs.hasNext()) {
                Map.Entry<String, JsonNode> keyValuePair = keyValuePairs.next();
                finalUrl = finalUrl.replace("{" + keyValuePair.getKey() + "}", URLEncoder.encode(keyValuePair.getValue().asText()));
            }

            return finalUrl;
        }

        return url;
    }

    public static HttpHeaders getDefaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-Agent", "cn.liangjw.caller/0.1");

        return headers;
    }

    public static HttpEntity<String> getRequestHttpEntity(CallerContext context) {
        String requestParam = "";

        if (context.getParam() != null
                && context.getApiItem().getParamType().equalsIgnoreCase("body")) {
            try {
                requestParam = mapper.writeValueAsString(context.getParam());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        return new HttpEntity<>(requestParam, context.getHttpHeaders());
    }
}
