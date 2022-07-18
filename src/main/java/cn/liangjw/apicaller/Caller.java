package cn.liangjw.apicaller;

import cn.liangjw.apicaller.models.ApiResult;
import cn.liangjw.apicaller.properties.CallerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class Caller {

    @Autowired
    private CallerProperties callerProperties;

    public ApiResult call(String method, Map<String, String> param) throws Exception {
        return call(method, param, RequestOption.getDefaultRequestOption());
    }

    public ApiResult call(String method, Map<String, String> param, RequestOption option) throws Exception {
        var context = new CallerContext(method, param, callerProperties, option);

        if (context.getApiItem().getNeedCache()) {
            if (option.isFromCache()) {
                context.setApiResult(option.getFromCache(context));
            }
        }

        if (context.getApiResult() != null) {
            option.log(context);
            return context.getApiResult();
        }

        try {
            RestTemplate restTemplate = new RestTemplate();

            var entity = restTemplate.exchange(context.getFinalUrl(),
                    context.getHttpMethod(),
                    context.getRequestHttpEntity(),
                    String.class);

            context.setApiResult(new ApiResult(entity));
            if (context.getApiItem().getNeedCache()) {
                option.setToCache(context);
            }

            return context.getApiResult();
        } catch (HttpClientErrorException ex) {
            return new ApiResult(ex.getStatusCode(), ex.getResponseBodyAsString());
        }
    }
}
