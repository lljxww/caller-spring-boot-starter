package cn.liangjw.apicaller.properties;

import lombok.Data;

import java.util.List;

@Data
public class ServiceItem {

    private String apiName;

    private String authorizationType = "none";

    private String baseUrl;

    private int timeout;

    private List<ApiItem> apiItems;
}

