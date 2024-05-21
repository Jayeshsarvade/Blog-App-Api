package com.blogappapi.useraddress.microservice.useraddressserviceblogappapi.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.blogappapi.useraddress.microservice.useraddressserviceblogappapi.dto.UserDto;

//http://localhost:9090/api/users/202
@FeignClient(name = "User-Service", url = "http://localhost:9090/api/users/")
public interface UserClient {

	@GetMapping("/{userId}")
	UserDto getAddressById(@PathVariable int userId);
	
}
