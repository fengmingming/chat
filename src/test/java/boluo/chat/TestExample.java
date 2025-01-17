package boluo.chat;

import boluo.chat.common.ResVo;
import boluo.chat.message.Message;
import boluo.chat.message.StringMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.util.AntPathMatcher;

import java.util.HashMap;

public class TestExample {

    @Test
    public void testAntMatcher() {
        AntPathMatcher matcher = new AntPathMatcher();
        System.out.println(matcher.match("/Tenants/{tenantId}", "/Tenants/123"));
    }

    @Test
    public void testJson() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        StringMessage message = new StringMessage();
        message.setContent("hello world");
        String json = om.writeValueAsString(ResVo.success(message));
        System.out.println(json);
        ResVo<Message> res = om.readValue(json, new TypeReference<ResVo<Message>>() {
        });
        System.out.println(res.getData().getClass());
    }

}
