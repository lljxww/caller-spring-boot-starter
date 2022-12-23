package cn.liangjw.apicaller;

import cn.liangjw.apicaller.models.ApiResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void LogTest() throws JsonProcessingException {
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

        public ESModel(String index, LogBase log) throws JsonProcessingException {
            this.index = index;
            jsonString = mapper.writeValueAsString(log);
        }
    }
}
