package com.smoke.xiguazi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class XiguaziApplication {

    public static void main(String[] args) {
        SpringApplication.run(XiguaziApplication.class, args);
    }

}
