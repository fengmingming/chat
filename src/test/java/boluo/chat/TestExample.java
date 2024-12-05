package boluo.chat;

import org.junit.jupiter.api.Test;
import org.springframework.util.AntPathMatcher;

public class TestExample {

    @Test
    public void testAntMatcher() {
        AntPathMatcher matcher = new AntPathMatcher();
        System.out.println(matcher.match("/Tenants/{tenantId}", "/Tenants/123"));
    }

}
