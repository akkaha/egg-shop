package cc.akkaha.shop.config;

import cc.akkaha.common.spring.MvcExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
public class AppConfig {

    @Bean
    HandlerExceptionResolver customExceptionResolver() {
        return new MvcExceptionHandler();
    }
}
