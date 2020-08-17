package com.integrationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableAsync;

import java.time.LocalDateTime;


@SpringBootApplication
@EnableEurekaClient
@EnableAsync
public class IntegrationServiceApplication {

    static final Integer i1 = 1;
    final Integer i2 = 2;
    Integer i3 = 3;

    public static void main(String[] args) {
        // String date = LocalDateTime.now().toString();
        //SpringApplication.run(IntegrationServiceApplication.class, args);
        final Integer i4 = 4;
        Integer i5 = 5;

        class Inner
        {
            final Integer i6 = 6;
            Integer i7 = 7;

            Inner()
            {
                System.out.print(i6 + i7);
            }
        }
    }

}
