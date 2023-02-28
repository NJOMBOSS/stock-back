package com.stockback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class StockBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockBackApplication.class, args);
	}

}
