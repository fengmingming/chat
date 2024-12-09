package boluo.chat.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Setter
@Getter
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(value = "boluo.chat")
public class ChatProperties {

    private String imAddress;
    private String groupKeyTemplate;
    private List<String> ignoreUrls = List.of();

}
