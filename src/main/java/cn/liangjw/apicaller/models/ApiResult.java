package cn.liangjw.apicaller.models;

import cn.liangjw.apicaller.utils.JsonNodeUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.io.Serializable;

public class ApiResult implements Serializable {
    @Getter
    private String raw;

    @Getter
    private HttpStatus statusCode;

    private JsonNode jsonNode;

    private ObjectMapper mapper = new ObjectMapper();

    public ApiResult(ResponseEntity<String> entity) {
        statusCode = entity.getStatusCode();

        try {
            raw = entity.getBody();
            jsonNode = mapper.readValue(raw, JsonNode.class);
        } catch (Exception ex) {

        }
    }

    public ApiResult(HttpStatus statusCode, String raw) {
        this.statusCode = statusCode;
        this.raw = raw;

        try{
            jsonNode = mapper.readValue(raw, JsonNode.class);
        } catch (Exception e) {
        }
    }

    public ApiResult(String raw) {
        this(HttpStatus.OK, raw);
    }

    public <T> T getValue(String key, Class<T> clazz) {
        if (key == null || key.isEmpty()) {
            return null;
        }

        if (jsonNode == null) {
            return null;
        }

        try {
            return JsonNodeUtil.getValueWithCaseInsensitiveKey(jsonNode, key, clazz);
        } catch (Exception ex) {
            return null;
        }
    }

    public <T> T toObject(Class<T> clazz) {
        if (jsonNode == null) {
            return null;
        }

        try {
            return mapper.treeToValue(jsonNode, clazz);
        } catch (Exception ex) {
            return null;
        }
    }
}
