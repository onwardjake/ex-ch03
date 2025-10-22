package com.jake.crudproj;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class CrudProjApplication {
	

	public static void main(String[] args) {
		SpringApplication.run(CrudProjApplication.class, args);
	}

}
