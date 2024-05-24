package com.blog.app.project.blogappapi.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.blog.app.project.blogappapi.dto.AddressDto;
import com.blog.app.project.blogappapi.dto.UserDto;
import com.blog.app.project.blogappapi.payload.UserResponse;
import com.blog.app.project.blogappapi.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {UserController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class UserControllerTest {
    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    /**
     * Method under test: {@link UserController#createUser(UserDto)}
     */

    private UserDto userDto;
    @BeforeEach
    public void setUp(){
        userDto = UserDto.builder()
                .about("About")
                .email("jane.doe@example.org")
                .id(1)
                .name("Name")
                .password("iloveyou")
                .build();
    }
    @Test
    void testCreateUser() throws Exception {
        // Arrange
        when(userService.createUser(Mockito.<UserDto>any())).thenReturn(userDto);

        UserDto userDto = new UserDto();
        userDto.setAbout("About");
        AddressDto addressDto = AddressDto.builder().city("Oxford").id(1).lane1("Lane1").lane2("Lane2").state("MD").build();
        userDto.setAddressDto(addressDto);
        userDto.setEmail("jane.doe@example.org");
        userDto.setId(1);
        userDto.setName("Name");
        userDto.setPassword("iloveyou");
        String content = (new ObjectMapper()).writeValueAsString(userDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController).build().perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"name\":\"Name\",\"email\":\"jane.doe@example.org\",\"password\":\"iloveyou\",\"about\":\"About\",\"addressDto"
                                        + "\":null}"));
    }

    /**
     * Method under test: {@link UserController#getUser(Integer)}
     */
    @Test
    void testGetUser() throws Exception {
        // Arrange
        when(userService.getUser(Mockito.<Integer>any())).thenReturn(userDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users/{userId}", 1);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"name\":\"Name\",\"email\":\"jane.doe@example.org\",\"password\":\"iloveyou\",\"about\":\"About\",\"addressDto"
                                        + "\":null}"));
    }

    /**
     * Method under test: {@link UserController#deleteUser(Integer)}
     */
    @Test
    void testDeleteUser() throws Exception {
        // Arrange
        doNothing().when(userService).deleteUser(Mockito.<Integer>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/users/{userId}", 1);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"message\":\"user deleted Successfully...\",\"success\":true}"));
    }

    /**
     * Method under test: {@link UserController#deleteUser(Integer)}
     */
    @Test
    void testDeleteUser2() throws Exception {
        // Arrange
        doNothing().when(userService).deleteUser(Mockito.<Integer>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/users/{userId}", 1);
        requestBuilder.contentType("https://example.org/example");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"message\":\"user deleted Successfully...\",\"success\":true}"));
    }

    /**
     * Method under test:
     * {@link UserController#getAllUsers(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllUsers() throws Exception {
        // Arrange
        UserResponse.UserResponseBuilder builderResult = UserResponse.builder();
        UserResponse buildResult = builderResult.content(new ArrayList<>())
                .lastPage(true)
                .pageNo(1)
                .pageSize(3)
                .totalElement(1L)
                .totalPages(1)
                .build();
        when(userService.gatAllUser(Mockito.<Integer>any(), Mockito.<Integer>any(), Mockito.<String>any(),
                Mockito.<String>any())).thenReturn(buildResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users/");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"content\":[],\"pageNo\":1,\"pageSize\":3,\"totalElement\":1,\"totalPages\":1,\"lastPage\":true}"));
    }

    /**
     * Method under test: {@link UserController#updateUser(UserDto, Integer)}
     */
    @Test
    void testUpdateUser() throws Exception {
        // Arrange
        when(userService.updateUSer(Mockito.<UserDto>any(), Mockito.<Integer>any())).thenReturn(userDto);

        UserDto userDto = new UserDto();
        userDto.setAbout("About");
        AddressDto addressDto = AddressDto.builder().city("Oxford").id(1).lane1("Lane1").lane2("Lane2").state("MD").build();
        userDto.setAddressDto(addressDto);
        userDto.setEmail("jane.doe@example.org");
        userDto.setId(1);
        userDto.setName("Name");
        userDto.setPassword("iloveyou");
        String content = (new ObjectMapper()).writeValueAsString(userDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/users/{userId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"name\":\"Name\",\"email\":\"jane.doe@example.org\",\"password\":\"iloveyou\",\"about\":\"About\",\"addressDto"
                                        + "\":null}"));
    }
}
