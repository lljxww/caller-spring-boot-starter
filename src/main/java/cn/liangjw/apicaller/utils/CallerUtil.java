package cn.liangjw.apicaller.utils;

import cn.liangjw.apicaller.CallerContext;
import cn.liangjw.apicaller.properties.ApiItem;
import cn.liangjw.apicaller.properties.CallerProperties;
import cn.liangjw.apicaller.properties.ServiceItem;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;

import java.net.URLEncoder;
import java.util.List;
import java.util.function.Predicate;

public class CallerUtil {
    public static <T> T firstOrDefault(List<T> source, Predicate<T> predicate) {
        if (source == null) {
            return null;
        }

        for (T item : source) {
            if (predicate.test(item)) {
                return item;
            }
        }

        return null;
    }

    public static String[] splitEndpointName(String method) {
        var nameArray = method.split("\\.");
        return nameArray;
    }

    public static ServiceItem getServiceItem(CallerProperties callerProperties, String serviceName) {
        var serviceItem = CallerUtil.firstOrDefault(callerProperties.getServiceItems(),
                i -> i.getApiName().equalsIgnoreCase(serviceName));
        return serviceItem;
    }

    public static ApiItem getApiItem(ServiceItem serviceItem, String apiName) {
        var apiItem = CallerUtil.firstOrDefault(serviceItem.getApiItems(),
                i -> i.getMethod().equalsIgnoreCase(apiName));
        return apiItem;
    }

    public static String getFinalUrl(String paramType, String url, @Nullable Object param) {
        JSONObject paramJObject = (JSONObject) JSONObject.toJSON(param);

        if ("query".equalsIgnoreCase(paramType)) {
            if (param == null) {
                return url;
            }

            String query = "?";

            for (var item : paramJObject.keySet()) {
                query += ("&" + item + "=" + URLEncoder.encode(String.valueOf(paramJObject.get(item))));
            }

            var finalUrl = url + query;

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

            var finalUrl = url;
            for (var item : paramJObject.keySet()) {
                finalUrl = finalUrl.replace("{" + item + "}", URLEncoder.encode(String.valueOf(paramJObject.get(item))));
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
            requestParam = JSON.toJSONString(context.getParam());
        }

        return new HttpEntity<>(requestParam, context.getHttpHeaders());
    }
}
