package cn.liangjw.apicaller;

import lombok.Getter;
import lombok.Setter;

import java.util.function.Function;

/**
 * @author liangjw
 * @version 1.0
 * Create at 018 07/18 10:50
 */
public class RequestOption {
    @Setter
    @Getter
    private boolean triggerOnExecuted;

    @Setter
    @Getter
    private boolean fromCache;

    @Setter
    @Getter
    private boolean dontLog;

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
}
