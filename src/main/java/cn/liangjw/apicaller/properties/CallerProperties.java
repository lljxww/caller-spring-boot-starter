package cn.liangjw.apicaller.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "spring.caller")
@Configuration
public class CallerProperties {
    private List<ServiceItem> serviceItems;
}

