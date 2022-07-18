package cn.liangjw.apicaller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CallerCallTest {
    @Autowired
    Caller caller;

    @Test
    public void GetTest() throws Exception {
        var param = new HashMap<String, String>();
        param.put("username", "liang1224");
        var result = caller.call("gh.getUserInfo", param);
        Assert.isTrue(result.getStatusCode() == HttpStatus.OK, "bad request");
    }
}
