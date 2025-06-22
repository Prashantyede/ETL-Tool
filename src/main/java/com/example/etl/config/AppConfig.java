package com.example.etl.config;

import com.example.etl.util.TransformConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;

@Configuration
public class AppConfig {

    @Bean
    public TransformConfig transformConfig() {
        return TransformConfig.loadFromYaml("transform-config.yml");
    }
} 
