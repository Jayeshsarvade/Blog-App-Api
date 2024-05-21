package com.blog.app.project.blogappapi.openfeignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.blog.app.project.blogappapi.dto.AddressDto;

//http://localhost:8080/address-app/address/152
@FeignClient(name = "Address",url = "http://localhost:7070")
public interface AddressClient {

	@GetMapping("/address/{id}")
	AddressDto getAddressByUserId(@PathVariable("id") int id);
	
	@DeleteMapping("/address/api/userId/{userId}")
	void deleteAddressByUserId(@PathVariable("userId") int userId);
	
}
