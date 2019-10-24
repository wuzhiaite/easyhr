package com.springboot.easyhr;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableTransactionManagement
@EnableSwagger2
@EnableAspectJAutoProxy
@EnableRabbit//
public class Easyhr_BackStage_Application{

    public static void main(String[] args) {
        SpringApplication.run(Easyhr_BackStage_Application.class,args);
    }
}
