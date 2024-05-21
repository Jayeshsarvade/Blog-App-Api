package com.blogappapi.useraddress.microservice.useraddressserviceblogappapi.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ApiResponse {

	String message;
	boolean success;
}
