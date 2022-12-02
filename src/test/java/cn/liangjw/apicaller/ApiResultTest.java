package cn.liangjw.apicaller;

import cn.liangjw.apicaller.models.ApiResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

/**
 * @author liangjw
 * @version 1.0
 * Create at 019 07/19 10:34
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiResultTest {
    @Test
    public void InitTest() {
        ApiResult apiResult = new ApiResult("{\"Success\":\"true\",\"login\":\"liang1224\",\"id\":12098363,\"node_id\":\"MDQ6VXNlcjEyMDk4MzYz\",\"avatar_url\":\"https://avatars.githubusercontent.com/u/12098363?v=4\",\"gravatar_id\":\"\",\"url\":\"https://api.github.com/users/liang1224\",\"html_url\":\"https://github.com/liang1224\",\"followers_url\":\"https://api.github.com/users/liang1224/followers\",\"following_url\":\"https://api.github.com/users/liang1224/following{/other_user}\",\"gists_url\":\"https://api.github.com/users/liang1224/gists{/gist_id}\",\"starred_url\":\"https://api.github.com/users/liang1224/starred{/owner}{/repo}\",\"subscriptions_url\":\"https://api.github.com/users/liang1224/subscriptions\",\"organizations_url\":\"https://api.github.com/users/liang1224/orgs\",\"repos_url\":\"https://api.github.com/users/liang1224/repos\",\"events_url\":\"https://api.github.com/users/liang1224/events{/privacy}\",\"received_events_url\":\"https://api.github.com/users/liang1224/received_events\",\"type\":\"User\",\"site_admin\":false,\"name\":\"Lljxww\",\"company\":null,\"blog\":\"liangjw.cn\",\"location\":\"Beijing\",\"email\":null,\"hireable\":null,\"bio\":\"O ever youthful,O ever weeping.\",\"twitter_username\":\"liangjw1224\",\"public_repos\":8,\"public_gists\":0,\"followers\":6,\"following\":11,\"created_at\":\"2015-04-24T11:11:55Z\",\"updated_at\":\"2022-07-19T00:17:07Z\"}");
        Assert.isTrue(apiResult.getValue("login", String.class).equalsIgnoreCase("liang1224"));
    }
}
