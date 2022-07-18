package cn.liangjw.apicaller.properties;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class AuthorizationProperties {

    private String name;

    private String authorizationInfo;
}
