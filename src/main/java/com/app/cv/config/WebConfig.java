package com.app.cv.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Adjust according to your endpoints
            .allowedOrigins("http://localhost:3000") // Allow localhost for development
            .allowedMethods("GET", "POST", "PUT", "DELETE") // Specify allowed methods
            .allowCredentials(true); // Allow credentials if needed
    }
}
