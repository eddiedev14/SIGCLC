package com.backend.sigclc;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SigclcApplication {
    public static void main(String[] args) {
        // Cargar variables del archivo .env
        Dotenv dotenv = Dotenv.load();
        System.setProperty("MONGO_URI", dotenv.get("MONGO_URI"));

        SpringApplication.run(SigclcApplication.class, args);
    }
}
