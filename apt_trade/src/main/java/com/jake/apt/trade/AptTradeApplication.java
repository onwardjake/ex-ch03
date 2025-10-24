package com.jake.apt.trade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
//@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class AptTradeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AptTradeApplication.class, args);
    }

}
