package cn.liangjw.apicaller.models;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResult {
    @Getter
    private String raw;

    @Getter
    private HttpStatus statusCode;

    private JSONObject jObject;

    public ApiResult(ResponseEntity<String> entity) {
        statusCode = entity.getStatusCode();

        try {
            raw = entity.getBody();
            jObject = (JSONObject) JSONObject.parse(raw);
        } catch (Exception ex) {

        }
    }

    public ApiResult(HttpStatus statusCode, String raw) {
        this.statusCode = statusCode;
        this.raw = raw;

        if (JSON.isValid(raw)) {
            jObject = (JSONObject) JSONObject.parse(raw);
        }
    }

    public ApiResult(String raw) {
        this(HttpStatus.OK, raw);
    }

    public <T> T getValue(String key, Class<T> clazz) {
        if (key == null || key.isEmpty()) {
            return null;
        }

        if (jObject == null) {
            return null;
        }

        String value = jObject.getString(key);

        try {
            return jObject.getObject(key, clazz);
        } catch (Exception ex) {
            return null;
        }
    }

    public <T> T toObject(Class<T> clazz) {
        if (jObject == null) {
            return null;
        }

        try {
            return jObject.toJavaObject(clazz);
        } catch (Exception ex) {
            return null;
        }
    }
}
