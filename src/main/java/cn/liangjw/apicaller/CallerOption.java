package cn.liangjw.apicaller;

import cn.liangjw.apicaller.models.ApiResult;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author liangjw
 * @version 1.0
 * Create at 019 07/19 15:41
 */
public class CallerOption {
    private static Map<String, Function<CallerContext, CallerContext>> authorizeFuncs = new HashMap<>();
    private static Function<CallerContext, ApiResult> readCacheFunction;
    private static Consumer<CallerContext> setToCacheConsumer;
    private static Consumer<CallerContext> logConsumer;

    public static void addAuthorizeFunc(String label, Function<CallerContext, CallerContext> func) {
        if (authorizeFuncs.containsKey(label)) {
            authorizeFuncs.remove(label);
        }

        authorizeFuncs.put(label, func);
    }

    static CallerContext getAuthorizedContext(CallerContext context) {
        String label = context.getAuthorizeType();

        if (!authorizeFuncs.containsKey(label)) {
            return context;
        }

        return authorizeFuncs.get(label).apply(context);
    }

    public static void addReadCacheFunction(Function<CallerContext, ApiResult> function) {
        readCacheFunction = function;
    }

    static ApiResult readCache(CallerContext context) {
        if (readCacheFunction != null) {
            return readCacheFunction.apply(context);
        }

        return null;
    }

    public static void addSetToCacheConsumer(Consumer<CallerContext> consumer) {
        setToCacheConsumer = consumer;
    }

    static void setCache(CallerContext context) {
        if (setToCacheConsumer != null) {
            setToCacheConsumer.accept(context);
        }
    }

    public static void addLogConsumer(Consumer<CallerContext> consumer) {
        logConsumer = consumer;
    }

    static void log(CallerContext context) {
        if (logConsumer != null) {
            logConsumer.accept(context);
        }
    }
}
