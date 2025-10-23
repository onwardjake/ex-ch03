package com.jake.mybatis.thymleaf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MybatisThymleafApplication {

	public static void main(String[] args) {
		SpringApplication.run(MybatisThymleafApplication.class, args);
	}

}
