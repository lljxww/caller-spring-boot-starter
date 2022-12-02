package cn.liangjw.apicaller;

import cn.liangjw.apicaller.models.ApiResult;
import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CallerCallTest {
    @Autowired
    Caller caller;

    @Test
    public void LogTest() {
        WebApiCallLog log = new WebApiCallLog();
        log.setMethod("test-method");

        ApiResult result = caller.call("es.SaveData", new ESModel("aams-j-webapicall", log));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    public class WebApiCallLog extends LogBase {
        private String method;
        private String url;
        private String httpMethod;
        private String paramType;
        private boolean success;
        private String param;
        private String result;
        private double runtime;
    }

    @Data
    public abstract class LogBase {
        private int id;
        private String userId;
        private String userName;
        private String source;
        private String message;
        private String description;
        private String hostIp;
        private String clientIp;
        private Date postTIme;
    }

    @Data
    public class ESModel {
        private String index;
        private String jsonString;

        public ESModel(String index, LogBase log) {
            this.index = index;
            jsonString = JSON.toJSONString(log);
        }
    }
}
