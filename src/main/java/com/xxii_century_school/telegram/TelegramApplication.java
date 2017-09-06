package com.xxii_century_school.telegram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
@EnableEurekaClient
public class TelegramApplication {
    public static void main(String[] args) {
        ApiContextInitializer.init();

        SpringApplication.run(TelegramApplication.class, args);
    }
}
