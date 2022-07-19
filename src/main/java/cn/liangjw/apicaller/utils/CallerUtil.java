package cn.liangjw.apicaller.utils;

import cn.liangjw.apicaller.CallerContext;
import cn.liangjw.apicaller.properties.ApiItem;
import cn.liangjw.apicaller.properties.CallerProperties;
import cn.liangjw.apicaller.properties.ServiceItem;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
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

    public static String[] splitEndpointName(String method) throws Exception {
        if (!method.contains(".")) {
            throw new Exception("bad method name");
        }

        var nameArray = method.split("\\.");
        if (nameArray.length != 2) {
            throw new Exception("bad method name");
        }

        return nameArray;
    }

    public static ServiceItem getServiceItem(CallerProperties callerProperties, String serviceName) throws Exception {
        var serviceItem = CallerUtil.firstOrDefault(callerProperties.getServiceItems(),
                i -> i.getApiName().equalsIgnoreCase(serviceName));

        if (serviceItem == null) {
            throw new Exception("can not find service \"" + serviceName + "\"");
        }

        return serviceItem;
    }

    public static ApiItem getApiItem(ServiceItem serviceItem, String apiName) throws Exception {
        var apiItem = CallerUtil.firstOrDefault(serviceItem.getApiItems(),
                i -> i.getMethod().equalsIgnoreCase(apiName));

        if (serviceItem == null) {
            throw new Exception("there is no such api \"" + apiName + "\" in service \""
                    + serviceItem.getApiName() + "\"");
        }

        return apiItem;
    }

    public static String getFinalUrl(String paramType, String url, @Nullable Map<String, String> param) throws UnsupportedEncodingException {
        if ("query".equalsIgnoreCase(paramType)) {
            if (param == null) {
                return url;
            }

            String query = "?";
            for (var item : param.keySet()) {
                query += ("&" + item + "=" + URLEncoder.encode(param.get(item), "UTF-8"));
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
            for (var item : param.keySet()) {
                finalUrl = finalUrl.replace("{" + item + "}", URLEncoder.encode(param.get(item), "UTF-8"));
            }

            return finalUrl;
        }

        return url;
    }

    public static HttpEntity<MultiValueMap<String, String>> getParamEntity(CallerContext context) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        HttpHeaders headers = context.getRequestOption().getAuthorizeHeader();

        if (context.getParam() != null
                && context.getApiItem().getParamType().equalsIgnoreCase("body")) {
            for (var item : context.getParam().keySet()) {
                map.add(item, context.getParam().get(item));
            }
        }

        return new HttpEntity<>(map, headers);
    }

    public static HttpHeaders getDefaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-Agent", "cn.liangjw.caller/0.1");

        return headers;
    }
}
