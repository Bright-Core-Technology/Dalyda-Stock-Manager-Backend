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


// TODO Load the Code on Git Action and make sure it works
// TODO Load the code on Docker and make sure it works
// TODO test the endpoints for errors and make sure they work as expected, test and see if it handles errors correctly, and make sure it returns the correct status codes and messages
// TODO clean code from all errors even the small yellow ones, and arrange the file structure
// TODO add all Sales Endpoints

// TODO put all the info in the application properties in the .env file and make it work.
// TODO change the application.properties in the test folder, and add the .env variables.
// TODO Add Comments to all the Code
