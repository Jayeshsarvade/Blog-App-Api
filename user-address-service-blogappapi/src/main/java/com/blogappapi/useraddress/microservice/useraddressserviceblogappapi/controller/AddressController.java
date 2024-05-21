package com.blogappapi.useraddress.microservice.useraddressserviceblogappapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blogappapi.useraddress.microservice.useraddressserviceblogappapi.dto.AddressDto;
import com.blogappapi.useraddress.microservice.useraddressserviceblogappapi.payload.AddressResponse;
import com.blogappapi.useraddress.microservice.useraddressserviceblogappapi.payload.ApiResponse;
import com.blogappapi.useraddress.microservice.useraddressserviceblogappapi.payload.AppConstants;
import com.blogappapi.useraddress.microservice.useraddressserviceblogappapi.service.AddressService;

import jakarta.validation.Valid;

@RestController
public class AddressController {

	@Autowired
	private AddressService addressService;

	@GetMapping("/address/{userId}")
	public ResponseEntity<AddressDto> getAddressByUserId(@PathVariable("userId") int userId) {

		AddressDto addressDto = addressService.findAddressByUserId(userId);
		return new ResponseEntity<AddressDto>(addressDto, HttpStatus.OK);
	}

	@GetMapping("/address/api/{id}")
	public ResponseEntity<AddressDto> getAddress(@PathVariable int id) {
		AddressDto address = addressService.getAddress(id);
		return new ResponseEntity<AddressDto>(address, HttpStatus.OK);
	}

	@DeleteMapping("/address/api/{id}")
	public ResponseEntity<ApiResponse> deleteAddress(@PathVariable int id) {
		addressService.deleteAddress(id);
		return new ResponseEntity<ApiResponse>(new ApiResponse("record deleted", true), HttpStatus.OK);
	}

	@DeleteMapping("/address/api/userId/{userId}")
	public ResponseEntity<ApiResponse> deleteAddressByUserId(@PathVariable int userId) {
		addressService.deleteAddressByUserId(userId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("address deleted with userId: " + userId, true),
				HttpStatus.OK);
	}

	@PutMapping("/address/api/{id}")
	public ResponseEntity<AddressDto> updateAddress(@Valid @RequestBody AddressDto addressDto, @PathVariable int id) {
		AddressDto updateAddress = addressService.updateAddress(addressDto, id);
		return new ResponseEntity<AddressDto>(updateAddress, HttpStatus.OK);
	}

	@GetMapping("/address/api/")
	public ResponseEntity<AddressResponse> getAllAddress(
			@RequestParam(value = "pageNo", defaultValue = AppConstants.PAGE_NO, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {
		 AddressResponse allAddress = addressService.getAllAddress(pageNo, pageSize, sortBy, sortDir);
		return new ResponseEntity<AddressResponse>(allAddress, HttpStatus.OK);
	}

	@PostMapping("/address/api/userId/{userId}")
	public ResponseEntity<AddressDto> createAddress(@Valid @RequestBody AddressDto addressDto, @PathVariable int userId) {
		AddressDto address = addressService.createAddress(addressDto, userId);
		return new ResponseEntity<AddressDto>(address, HttpStatus.CREATED);
	}

}
