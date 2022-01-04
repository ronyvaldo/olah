package com.olah.clients.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public CommandLineRunner execute() {
        return args -> {
            System.out.println("==========================================");
            System.out.println("Contexto da aplicação Olah inicializado!");
            System.out.println("==========================================");
        };
    }
}
