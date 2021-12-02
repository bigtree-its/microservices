package com.bigtree.fapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableJpaAuditing
@EnableFeignClients
@SpringBootApplication
public class FAPIApplication {

	public static void main(String[] args) {
		SpringApplication.run(FAPIApplication.class, args);
	}

}
