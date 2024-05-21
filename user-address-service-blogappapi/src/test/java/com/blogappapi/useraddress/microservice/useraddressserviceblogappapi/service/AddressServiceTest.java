package com.blogappapi.useraddress.microservice.useraddressserviceblogappapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockitoSession;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.blogappapi.useraddress.microservice.useraddressserviceblogappapi.Exception.ResourceNotFoundException;
import com.blogappapi.useraddress.microservice.useraddressserviceblogappapi.dto.AddressDto;
import com.blogappapi.useraddress.microservice.useraddressserviceblogappapi.entity.Address;
import com.blogappapi.useraddress.microservice.useraddressserviceblogappapi.repository.AddressRepository;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

	
	@InjectMocks
	private AddressService addressService;

	@Mock
	private AddressRepository addressRepository;

	
	private Address address1;
	private Address address2;
	private Address address3;
	private AddressDto addressDto;
	
	@BeforeEach
	public void setUp()throws Exception{
		
		address1 = Address.builder().id(1).lane1("dummy lane1").lane2("dummy lane2")
				.city("xxxx").state("xxxx").build();
		address2 = Address.builder().id(2).lane1("dummy lane1").lane2("dummy lane2")
				.city("xxxx").state("xxxx").build();
		address3 = Address.builder().id(2).lane1("dummy lane1").lane2("dummy lane2")
				.city("xxxx").state("xxxx").build();
		
		addressDto  = new AddressDto();
		addressDto.setId(1);
		addressDto.setLane1("dummy lane1");
		addressDto.setLane2("dummy lane2");
		addressDto.setCity("xxxx");
		addressDto.setState("xxxx");
	}
	
	@Test
	public void testGetAddressById_success()throws Exception{
        int addressId = 1;

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address1));
        AddressDto addressDto = addressService.getAddress(addressId);
        
        Mockito.verify(addressRepository, Mockito.times(1)).findById(addressId);

        assertEquals(address1.getId(), addressDto.getId());
        assertEquals(address1.getLane1(), addressDto.getLane1());
        assertEquals(address1.getLane2(), addressDto.getLane2());
        assertEquals(address1.getCity(), addressDto.getCity());
        assertEquals(address1.getState(), addressDto.getState());
	}
	
	@Test
	public void testGetAddressById_NotFound()throws Exception{
		int addressId = 1;
		when(addressRepository.findById(addressId)).thenThrow(new ResourceNotFoundException("address","addressId", addressId));
		assertThrows(ResourceNotFoundException.class, ()-> addressService.getAddress(addressId));
		Mockito.verify(addressRepository, Mockito.times(1)).findById(addressId);
	}
	
	@Test
	public void testGetAddressByUserId()throws Exception{
        int userId = 1;

        when(addressRepository.findAddressByUserId(userId)).thenReturn(address1);
        AddressDto addressDto = addressService.findAddressByUserId(userId);
        Mockito.verify(addressRepository, Mockito.times(1)).findAddressByUserId(userId);

        assertEquals(address1.getId(), addressDto.getId());
        assertEquals(address1.getLane1(), addressDto.getLane1());
        assertEquals(address1.getLane2(), addressDto.getLane2());
        assertEquals(address1.getCity(), addressDto.getCity());
        assertEquals(address1.getState(), addressDto.getState());
	}
	
	@Test
	public void testGetAddressByUserId_NotFound()throws Exception{
		when(addressRepository.findAddressByUserId(1)).thenThrow(new ResourceNotFoundException("address","addressId",1));
		assertThrows(ResourceNotFoundException.class,()-> addressService.findAddressByUserId(1));
		Mockito.verify(addressRepository, Mockito.times(1)).findAddressByUserId(1);

	}
	
	@Test
	public void testDeleteAddress_success() throws Exception{
		when(addressRepository.findById(1)).thenReturn(Optional.of(address1));
		addressService.deleteAddress(1);
		Mockito.verify(addressRepository, Mockito.times(1)).findById(1);

	}
	
	@Test
	public void testDeleteAddress_NotFound() throws Exception{
		when(addressRepository.findById(1)).thenThrow(new ResourceNotFoundException("address","address1",1));
		assertThrows(ResourceNotFoundException.class, ()-> addressService.deleteAddress(1));
		Mockito.verify(addressRepository, Mockito.times(1)).findById(1);
	}
	
	@Test
	public void testUpdateAddress() throws Exception{
        int addressId = 1;

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address1));
        when(addressRepository.save(any(Address.class))).thenReturn(address1);

        AddressDto updatedAddressDto = addressService.updateAddress(addressDto, addressId);
        
		Mockito.verify(addressRepository, Mockito.times(1)).findById(addressId);
		Mockito.verify(addressRepository, Mockito.times(1)).save(any(Address.class));


        assertEquals(addressId, updatedAddressDto.getId());
        assertEquals(addressDto.getLane1(), updatedAddressDto.getLane1());
        assertEquals(addressDto.getLane2(), updatedAddressDto.getLane2());
        assertEquals(addressDto.getCity(), updatedAddressDto.getCity());
        assertEquals(addressDto.getState(), updatedAddressDto.getState());
	}
	
	@Test
	public void testUpdateAddress_NotFound()throws Exception{
		int addressId = 1;
		when(addressRepository.findById(addressId)).thenThrow(new ResourceNotFoundException("address","addressId",addressId));
		assertThrows(ResourceNotFoundException.class,()->addressService.updateAddress(addressDto, addressId));
		Mockito.verify(addressRepository, Mockito.times(1)).findById(addressId);

	}
	

	
}
