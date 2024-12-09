package boluo.chat;

import boluo.chat.exception.DefaultExceptionResolver;
import boluo.chat.filter.ValidTokenFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@SpringBootApplication
@MapperScan(basePackages = "boluo.chat.mapper")
public class Bootstrap implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class, args);
    }

    @Bean
    public ValidTokenFilter validTokenFilter() {
        return new ValidTokenFilter();
    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(new DefaultExceptionResolver());
    }

}
