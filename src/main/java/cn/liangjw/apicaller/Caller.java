package cn.liangjw.apicaller;

import cn.liangjw.apicaller.models.ApiResult;
import cn.liangjw.apicaller.properties.CallerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
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
        CallerContext context = CallerContext.build(callerProperties, method, param, option);

        if (context.getApiItem().getNeedCache()) {
            if (context.getRequestOption().isFromCache()) {
                context.setApiResult(CallerOption.readCache(context));
            }
        }

        ApiResult apiResult = null;

        if (context.getApiResult() != null) {
            context.setRuntime(0);
            context.setSource("c");
            CallerOption.log(context);
            apiResult = context.getApiResult();
        }

        if (apiResult == null) {
            StopWatch sw = new StopWatch();

            try {
                RestTemplate restTemplate = new RestTemplate();
                sw.start();
                ResponseEntity<String> entity = restTemplate.exchange(context.getFinalUrl(),
                        context.getHttpMethod(),
                        context.getHttpEntity(),
                        String.class);
                sw.stop();

                context.setRuntime(sw.getTotalTimeSeconds());
                context.setSource("r");

                context.setApiResult(new ApiResult(entity));
                if (context.getApiItem().getNeedCache()) {
                    CallerOption.setCache(context);
                }

                apiResult = context.getApiResult();
            } catch (HttpClientErrorException ex) {
                apiResult = new ApiResult(ex.getStatusCode(), ex.getResponseBodyAsString());
            } finally {
                if (sw.isRunning()) {
                    sw.stop();
                }
            }
        }

        CallerOption.log(context);

        return apiResult;
    }
}
