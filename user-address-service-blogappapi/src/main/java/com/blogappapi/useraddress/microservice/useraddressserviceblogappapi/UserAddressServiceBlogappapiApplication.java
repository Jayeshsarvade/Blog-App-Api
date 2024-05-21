package com.blogappapi.useraddress.microservice.useraddressserviceblogappapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class UserAddressServiceBlogappapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserAddressServiceBlogappapiApplication.class, args);
	}

}
