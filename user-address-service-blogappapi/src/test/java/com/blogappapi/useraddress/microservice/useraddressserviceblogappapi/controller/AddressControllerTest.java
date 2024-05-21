package com.blogappapi.useraddress.microservice.useraddressserviceblogappapi.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.blogappapi.useraddress.microservice.useraddressserviceblogappapi.entity.Address;
import com.blogappapi.useraddress.microservice.useraddressserviceblogappapi.repository.AddressRepository;
import com.blogappapi.useraddress.microservice.useraddressserviceblogappapi.service.AddressService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(AddressController.class)
@AutoConfigureMockMvc
class AddressControllerTest {


    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    @InjectMocks
    private AddressController addressController;

    @MockBean
    private AddressRepository addressRepository;
    
    @MockBean
    AutoCloseable autoCloseable;
    
	
    private Address address;
    private Address address1;
    private Address address2;

    @BeforeEach
    public void setUp() {
    	address = Address.builder().id(1).lane1("dummy lane1").lane2("dummy lane2")
    			.city("dummy city").state("dummy state").build();
    	
    	address1 = Address.builder().id(1).lane1("dummy lane1").lane2("dummy lane2")
    			.city("dummy city").state("dummy state").build();
    	
    	address2 = Address.builder().id(1).lane1("dummy lane1").lane2("dummy lane2")
    			.city("dummy city").state("dummy state").build();
    	
    }
    
    @AfterEach
    public void tearDown() throws Exception{
    	autoCloseable.close();
    }
    
    @Test
    public void testGetAddressById() throws Exception{
    	
    	Mockito.when(addressRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(address));
    	
    	mockMvc.perform(MockMvcRequestBuilders
    			.get("/address/api/11")
    			.contentType(MediaType.APPLICATION_JSON_VALUE))
    			.andExpect(status().isOk());	
    } 
    
    @Test
    public void testGetAddressByUserId() throws Exception{
    	Mockito.when(addressRepository.findAddressByUserId(Mockito.anyInt())).thenReturn(address);
    	
    	mockMvc.perform(MockMvcRequestBuilders
    			.get("/address/1")
    			.content(MediaType.APPLICATION_JSON_VALUE))
    			.andExpect(status().isOk());
    }
    
    @Test
    public void testDeleteAddress()throws Exception{
    	Mockito.when(addressRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(address));
    	
    	mockMvc.perform(MockMvcRequestBuilders
    			.delete("/address/api/1")
    			.contentType(MediaType.APPLICATION_JSON_VALUE))
    			.andExpect(status().isOk());				
    }
    
    @Test
    public void testUpdateAddress() throws Exception{
    	
    	Address address1 = new Address(1,"dummylane1","dummyLane2","pune", "maharashtra",1);
    	Mockito.when(addressRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(address));
    	Mockito.when(addressRepository.save(address1)).thenReturn(address1);
    	
    	   String updatedContent = objectWriter.writeValueAsString(address1);

           MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                   .put("/address/api/1")
                   .contentType(MediaType.APPLICATION_JSON)
                   .accept(MediaType.APPLICATION_JSON)
                   .content(updatedContent);

           mockMvc.perform(mockRequest)
                   .andExpect(status().isOk())
                   .andReturn();
    }
    
    @Test
    public void testGetAllAddress()throws Exception{
    	
    	List<Address> addressList = new ArrayList<>(); 
    	addressList.add(address);
    	addressList.add(address1);
    	addressList.add(address2);
    	
    	Mockito.when(addressRepository.findAll()).thenReturn(addressList);
    	

    	mockMvc.perform(MockMvcRequestBuilders
    			.get("/address/api/")
    			.contentType(MediaType.APPLICATION_JSON_VALUE))
    			.andExpect(status().isOk());
    }
}
