package com.example.dalyda_backend_stockmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DalydaBackendStockManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DalydaBackendStockManagerApplication.class, args);
    }

}
