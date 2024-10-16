package com.example;

import com.example.config.PropertiesConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class MsSpringBootApplication implements CommandLineRunner {

    private final PropertiesConfig propertiesConfig;

    @Value("${has.account}")
    private boolean hasAccount;

    public static void main(String[] args) {
        SpringApplication.run(MsSpringBootApplication.class, args);
    }

    @Override
    public void run(String... args) {
        if (hasAccount) {
            log.info("hasAccount");
            propertiesConfig.getNames().forEach(System.out::println);
        } else {
            log.info("Does not possess any accounts");
        }
    }
}