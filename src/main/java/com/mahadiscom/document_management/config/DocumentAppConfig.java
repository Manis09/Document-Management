package com.mahadiscom.document_management.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocumentAppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
