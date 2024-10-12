package com.example.config;

import com.example.mapper.StudentMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class MapperConfiguration {

    @Bean
    public StudentMapper studentMapper() {
        return StudentMapper.INSTANCE;
    }
}