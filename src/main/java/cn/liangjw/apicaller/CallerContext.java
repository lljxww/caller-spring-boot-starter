package cn.liangjw.apicaller;

import cn.liangjw.apicaller.models.ApiResult;
import cn.liangjw.apicaller.properties.ApiItem;
import cn.liangjw.apicaller.properties.CallerProperties;
import cn.liangjw.apicaller.properties.ServiceItem;
import cn.liangjw.apicaller.utils.CallerUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;

@Getter
public class CallerContext {
    private String method;

    private String serviceName;

    private String apiName;

    private ServiceItem serviceItem;

    private ApiItem apiItem;

    private Object param;

    private String jsonParam;

    private RequestOption requestOption;

    private String finalUrl;

    @Setter(AccessLevel.MODULE)
    private double runtime;

    @Setter(AccessLevel.MODULE)
    private String source;

    @Setter
    private HttpHeaders httpHeaders;

    private HttpEntity<String> httpEntity;

    @Setter(AccessLevel.MODULE)
    private ApiResult apiResult;

    private CallerContext() {
    }

    public static CallerContext build(CallerProperties callerProperties, String method, @Nullable Object param,
                                      @Nullable RequestOption requestOption) {
        CallerContext context = new CallerContext();

        context.method = method;
        String[] names = CallerUtil.splitEndpointName(method);
        context.serviceName = names[0];
        context.apiName = names[1];
        context.serviceItem = CallerUtil.getServiceItem(callerProperties, context.serviceName);
        context.apiItem = CallerUtil.getApiItem(context.serviceItem, context.apiName);
        context.param = param;
        context.jsonParam = param == null ? "" : JSONObject.toJSONString(param);
        context.requestOption = requestOption != null ? requestOption : RequestOption.getDefaultRequestOption();

        // 以下步骤包含请求授权配置, 不要调整顺序
        context.httpHeaders = CallerUtil.getDefaultHeaders();
        if (context.apiItem.getContentType() == null || context.apiItem.getContentType().isEmpty()) {
            context.apiItem.setContentType("application/json");
        }
        context.httpHeaders.add("Content-Type", context.apiItem.getContentType());
        context.finalUrl = context.getFinalUrl();
        context = CallerOption.getAuthorizedContext(context);
        context.httpEntity = new HttpEntity<>(context.getJsonParam(), context.getHttpHeaders());

        return context;
    }

    public @Nullable String getAuthorizeType() {
        if (apiItem.getAuthorizationType() != null) {
            return apiItem.getAuthorizationType();
        }

        return serviceItem.getAuthorizationType();
    }

    public String getFinalUrl() {
        return CallerUtil.getFinalUrl(apiItem.getParamType(), serviceItem.getBaseUrl() + apiItem.getUrl(), param);
    }

    public HttpMethod getHttpMethod() {
        return HttpMethod.valueOf(apiItem.getHttpMethod().toUpperCase());
    }
}