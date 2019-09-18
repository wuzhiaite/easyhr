package com.springboot.easyhr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class Easyhr_QuartzApplication {

    public static void main(String[] args) {
        SpringApplication.run(Easyhr_QuartzApplication.class,args);
    }
}
