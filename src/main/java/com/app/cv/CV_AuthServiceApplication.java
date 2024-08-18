package com.app.cv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CV_AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CV_AuthServiceApplication.class, args);
        System.out.println("CV_AuthServiceApplication Successfully started...!!!");
    }

}
