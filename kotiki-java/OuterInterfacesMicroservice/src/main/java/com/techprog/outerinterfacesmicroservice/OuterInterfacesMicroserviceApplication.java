package com.techprog.outerinterfacesmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.techprog.entities")
public class OuterInterfacesMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OuterInterfacesMicroserviceApplication.class, args);
        System.out.println();
    }

}