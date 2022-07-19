package cn.liangjw.apicaller;

import cn.liangjw.apicaller.models.ApiResult;
import cn.liangjw.apicaller.properties.ApiItem;
import cn.liangjw.apicaller.properties.CallerProperties;
import cn.liangjw.apicaller.properties.ServiceItem;
import cn.liangjw.apicaller.utils.CallerUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class CallerContext {
    private String method;

    private String serviceName;

    private String apiName;

    private ServiceItem serviceItem;

    private ApiItem apiItem;

    private Map<String, String> param;

    private RequestOption requestOption;

    private ApiResult apiResult;

    public CallerContext(String method, @Nullable Map<String, String> param, CallerProperties callerProperties, @Nullable RequestOption requestOption)
            throws Exception {
        this.method = method;
        var names = CallerUtil.splitEndpointName(method);
        serviceName = names[0];
        apiName = names[1];
        this.serviceItem = CallerUtil.getServiceItem(callerProperties, serviceName);
        this.apiItem = CallerUtil.getApiItem(serviceItem, apiName);
        this.param = param;
        this.requestOption = requestOption == null ? RequestOption.getDefaultRequestOption() : requestOption;
    }

    public String getFinalUrl() throws UnsupportedEncodingException {
        return CallerUtil.getFinalUrl(apiItem.getParamType(), serviceItem.getBaseUrl() + apiItem.getUrl(), param);
    }

    public HttpMethod getHttpMethod() {
        return HttpMethod.valueOf(apiItem.getHttpMethod().toUpperCase());
    }

    public HttpEntity<MultiValueMap<String, String>> getRequestHttpEntity() {
        return CallerUtil.getParamEntity(this);
    }
}