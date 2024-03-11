package com.simblog.api;

import com.simblog.api.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
public class SimblogApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimblogApplication.class, args);
    }

}
