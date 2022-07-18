package cn.liangjw.apicaller.models;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
public class ApiResult {

    private boolean success;

    private String message;

    private int code;

    private String raw;

    private HttpStatus statusCode;

    public ApiResult(ResponseEntity<String> entity) {
        statusCode = entity.getStatusCode();

        try {
            raw = entity.getBody();
        } catch (Exception ex) {

        }
    }

    public ApiResult(HttpStatus statusCode, String body) {
        this.statusCode = statusCode;
        raw = body;
    }
}
