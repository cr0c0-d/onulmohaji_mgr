package me.croco.onulmohaji;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OnulMohajiApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnulMohajiApplication.class, args);
    }
}