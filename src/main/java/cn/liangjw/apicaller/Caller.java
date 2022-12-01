package cn.liangjw.apicaller;

import cn.liangjw.apicaller.models.ApiResult;
import cn.liangjw.apicaller.properties.CallerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class Caller {

    @Autowired
    private CallerProperties callerProperties;

    public ApiResult call(String method) {
        return call(method, null);
    }

    public ApiResult call(String method, @Nullable Object param) {
        return call(method, param, null);
    }

    public ApiResult call(String method, @Nullable Object param, @Nullable RequestOption option) {
        var context = CallerContext.build(callerProperties, method, param, option);

        if (context.getApiItem().getNeedCache()) {
            if (context.getRequestOption().isFromCache()) {
                context.setApiResult(CallerOption.readCache(context));
            }
        }

        if (context.getApiResult() != null) {
            CallerOption.log(context);
            return context.getApiResult();
        }

        try {
            RestTemplate restTemplate = new RestTemplate();

            var entity = restTemplate.exchange(context.getFinalUrl(),
                    context.getHttpMethod(),
                    context.getHttpEntity(),
                    String.class);

            context.setApiResult(new ApiResult(entity));
            if (context.getApiItem().getNeedCache()) {
                CallerOption.setCache(context);
            }

            return context.getApiResult();
        } catch (HttpClientErrorException ex) {
            return new ApiResult(ex.getStatusCode(), ex.getResponseBodyAsString());
        }
    }
}
