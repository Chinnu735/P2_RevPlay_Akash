package com.revplay.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir:uploads/songs}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String absolutePath = Paths.get(uploadDir).toAbsolutePath().normalize().toUri().toString();
        // Fallback or explicit mapping just in case
        if (absolutePath.endsWith("/songs/")) {
            absolutePath = absolutePath.replace("/songs/", "/");
        }
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(absolutePath);
    }
}
