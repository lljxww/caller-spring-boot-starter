package cn.liangjw.apicaller;

import cn.liangjw.apicaller.models.ApiResult;
import lombok.Data;
import org.springframework.http.HttpHeaders;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author liangjw
 * @version 1.0
 * Create at 018 07/18 10:50
 */
@Data
public class RequestOption {
    private boolean triggerOnExecuted;

    private boolean fromCache;

    private boolean dontLog;

    private String customAuthorizeInfo;

    private Function<HttpHeaders, HttpHeaders> authorizeHeaderFunc;

    private Function<CallerContext, ApiResult> getFromCacheFunc;

    private Consumer<CallerContext> setToCacheCus;

    private Consumer<CallerContext> logCus;

    private RequestOption() {
    }

    public static RequestOption build(Function<RequestOption, RequestOption> func) {
        var option = getDefaultRequestOption();
        return func.apply(option);
    }

    static RequestOption getDefaultRequestOption() {
        RequestOption option = new RequestOption();

        option.triggerOnExecuted = true;
        option.fromCache = true;
        option.dontLog = false;

        return option;
    }

    public HttpHeaders getAuthorizeHeader(HttpHeaders headers) {
        if (authorizeHeaderFunc != null) {
            headers = authorizeHeaderFunc.apply(headers);
        }

        return headers;
    }

    public ApiResult getFromCache(CallerContext context) {
        if (getFromCacheFunc != null) {
            return getFromCacheFunc.apply(context);
        }

        return null;
    }

    public void setToCache(CallerContext context) {
        if (setToCacheCus != null) {
            setToCacheCus.accept(context);
        }
    }

    public void log(CallerContext context) {
        if (logCus != null) {
            logCus.accept(context);
        }
    }
}
