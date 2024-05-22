package com.blogappapi.useraddress.microservice.useraddressserviceblogappapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class UserAddress{

	public static void main(String[] args) {
		SpringApplication.run(UserAddress.class, args);
	}

}
