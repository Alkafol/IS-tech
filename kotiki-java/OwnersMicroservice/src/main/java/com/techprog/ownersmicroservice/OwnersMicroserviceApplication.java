package com.techprog.ownersmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EntityScan("com.techprog.entities")
public class OwnersMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OwnersMicroserviceApplication.class, args);
    }

}
