package org.traditionalAlcohol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TraditionalAlcoholApplication {
    public static void main(String[] args) {
        SpringApplication.run(TraditionalAlcoholApplication.class, args);}
    }