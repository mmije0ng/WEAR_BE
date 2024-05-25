package com.backend.wear;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@EnableScheduling //스케줄러
@SpringBootApplication
@EntityScan("com.backend.wear.entity")
public class WearApplication {
	public static void main(String[] args) {
		SpringApplication.run(WearApplication.class, args);
	}
}