package com.example;

import com.example.config.PropertiesConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class MsSpringBootApplication implements CommandLineRunner {

    private final PropertiesConfig propertiesConfig;

    @Value("${has.account}")
    private boolean hasAccount;

    public static void main(String[] args) {
        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication app = new SpringApplication(MsSpringBootApplication.class);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    private static void logApplicationStartup(Environment env) {
        String serverPort = env.getProperty("local.server.port");
        log.info("Application is running in `{}` port, profile: {}", serverPort, env.getActiveProfiles());
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