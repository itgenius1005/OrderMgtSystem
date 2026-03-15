package com.itgenius.ordermgtsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OrderMgtSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderMgtSystemApplication.class, args);
    }
}
