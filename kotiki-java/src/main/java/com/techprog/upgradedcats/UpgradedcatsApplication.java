package com.techprog.upgradedcats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication()
@EnableJpaRepositories()
public class UpgradedcatsApplication {

    public static void main(String[] args) {
        SpringApplication.run(UpgradedcatsApplication.class, args);
    }

}
