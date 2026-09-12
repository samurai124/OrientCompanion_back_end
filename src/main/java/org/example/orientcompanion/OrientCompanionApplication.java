package org.example.orientcompanion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class OrientCompanionApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrientCompanionApplication.class, args);
    }

}
