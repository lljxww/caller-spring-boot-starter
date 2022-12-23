package cn.liangjw.apicaller.properties;

import lombok.Data;

@Data
public class ApiItem {

    private String method;

    private String url;

    private String httpMethod;

    private String paramType;

    private String description;

    private Boolean needCache = false;

    private int cacheTime = 10;

    private String contentType;

    private String authorizationType;

    private int timeout = 30;
}
