package cn.liangjw.apicaller;

import cn.liangjw.apicaller.properties.CallerProperties;
import cn.liangjw.apicaller.properties.ServiceItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PropertiesInjectTest {
    @Autowired
    private CallerProperties callerProperties;

    @Test
    public void InjectTest() {
        List<ServiceItem> services = callerProperties.getServiceItems();
    }
}
