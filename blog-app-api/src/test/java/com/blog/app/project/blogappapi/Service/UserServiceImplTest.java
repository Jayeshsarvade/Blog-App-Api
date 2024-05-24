package com.blog.app.project.blogappapi.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blog.app.project.blogappapi.dto.AddressDto;
import com.blog.app.project.blogappapi.dto.UserDto;
import com.blog.app.project.blogappapi.entity.User;
import com.blog.app.project.blogappapi.exception.ResourceNotFoundException;
import com.blog.app.project.blogappapi.openfeignclient.AddressClient;
import com.blog.app.project.blogappapi.payload.UserResponse;
import com.blog.app.project.blogappapi.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import com.blog.app.project.blogappapi.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class UserServiceImplTest {
    @MockBean
    private AddressClient addressClient;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;

    /**
     * Method under test: {@link UserServiceImpl#createUser(UserDto)}
     */

    private User user;
    private AddressDto addressDto;
    private User user2;

    @BeforeEach
    public void setUp() {

        user = new User();
        user.setAbout("About");
        user.setComments(new HashSet<>());
        user.setEmail("jane.doe@example.org");
        user.setId(1);
        user.setName("Name");
        user.setPassword("iloveyou");
        user.setPost(new ArrayList<>());

        user2 = new User();
        user2.setAbout("About");
        user2.setComments(new HashSet<>());
        user2.setEmail("jane.doe@example.org");
        user2.setId(1);
        user2.setName("Name");
        user2.setPassword("iloveyou");
        user2.setPost(new ArrayList<>());

        addressDto = AddressDto.builder()
                .city("Oxford")
                .id(1)
                .lane1("Lane1")
                .lane2("Lane2")
                .state("MD")
                .build();
    }

    @Test
    void testCreateUser() {
        // Arrange
        when(userRepository.save(Mockito.<User>any())).thenReturn(user);
        UserDto actualCreateUserResult = userServiceImpl.createUser(new UserDto());
        // Assert
        verify(userRepository).save(isA(User.class));
        assertNull(actualCreateUserResult.getAddressDto());
        assertNull(actualCreateUserResult.getAbout());
        assertNull(actualCreateUserResult.getEmail());
        assertNull(actualCreateUserResult.getName());
        assertNull(actualCreateUserResult.getPassword());
        assertEquals(0, actualCreateUserResult.getId());
    }

    /**
     * Method under test: {@link UserServiceImpl#updateUSer(UserDto, Integer)}
     */
    @Test
    void testUpdateUSer() {
        // Arrange
        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);
        when(userRepository.save(Mockito.<User>any())).thenReturn(user2);
        when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        // Act
        UserDto actualUpdateUSerResult = userServiceImpl.updateUSer(new UserDto(), 1);
        // Assert
        verify(addressClient).getAddressByUserId(eq(1));
        verify(userRepository).findById(eq(1));
        verify(userRepository).save(isA(User.class));
        assertEquals("Oxford", actualUpdateUSerResult.getAddressDto().getCity());
        assertNull(actualUpdateUSerResult.getAbout());
        assertNull(actualUpdateUSerResult.getEmail());
        assertNull(actualUpdateUSerResult.getName());
        assertNull(actualUpdateUSerResult.getPassword());
        assertEquals(1, actualUpdateUSerResult.getId());
    }

    /**
     * Method under test: {@link UserServiceImpl#updateUSer(UserDto, Integer)}
     */
    @Test
    void testUpdateUSer2() {
        // Arrange
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);
        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.updateUSer(new UserDto(), 1));
        verify(userRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link UserServiceImpl#getUser(Integer)}
     */
    @Test
    void testGetUser() {
        // Arrange
        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);
        when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        // Act
        UserDto actualUser = userServiceImpl.getUser(1);
        // Assert
        verify(addressClient).getAddressByUserId(eq(1));
        verify(userRepository).findById(eq(1));
        assertEquals("About", actualUser.getAbout());
        assertEquals("Name", actualUser.getName());
        assertEquals("Oxford", actualUser.getAddressDto().getCity());
        assertEquals("iloveyou", actualUser.getPassword());
        assertEquals("jane.doe@example.org", actualUser.getEmail());
        assertEquals(1, actualUser.getId());
    }

    /**
     * Method under test: {@link UserServiceImpl#getUser(Integer)}
     */
    @Test
    void testGetUser2() {
        // Arrange
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.getUser(1));
        verify(userRepository).findById(eq(1));
    }

    /**
     * Method under test:
     * {@link UserServiceImpl#gatAllUser(Integer, Integer, String, String)}
     */
    @Test
    void testGatAllUser() {
        // Arrange
        ArrayList<User> content = new ArrayList<>();
        when(userRepository.findAll(Mockito.<Pageable>any())).thenReturn(new PageImpl<>(content));
        // Act
        UserResponse actualGatAllUserResult = userServiceImpl.gatAllUser(1, 3, "Sort By", "Sort Dir");

        // Assert
        verify(userRepository).findAll(isA(Pageable.class));
        assertEquals(0, actualGatAllUserResult.getPageNo());
        assertEquals(0, actualGatAllUserResult.getPageSize());
        assertEquals(0L, actualGatAllUserResult.getTotalElement());
        assertEquals(1, actualGatAllUserResult.getTotalPages());
        assertTrue(actualGatAllUserResult.isLastPage());
        assertEquals(content, actualGatAllUserResult.getContent());
    }

    /**
     * Method under test:
     * {@link UserServiceImpl#gatAllUser(Integer, Integer, String, String)}
     */
    @Test
    void testGatAllUser2() {
        // Arrange
        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        ArrayList<User> content = new ArrayList<>();
        content.add(user);
        PageImpl<User> pageImpl = new PageImpl<>(content);
        when(userRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);
        // Act
        UserResponse actualGatAllUserResult = userServiceImpl.gatAllUser(1, 3, "Sort By", "Sort Dir");
        // Assert
        verify(addressClient).getAddressByUserId(eq(1));
        verify(userRepository).findAll(isA(Pageable.class));
        assertEquals(0, actualGatAllUserResult.getPageNo());
        assertEquals(1, actualGatAllUserResult.getPageSize());
        assertEquals(1, actualGatAllUserResult.getTotalPages());
        assertEquals(1, actualGatAllUserResult.getContent().size());
        assertEquals(1L, actualGatAllUserResult.getTotalElement());
        assertTrue(actualGatAllUserResult.isLastPage());
    }
    /**
     * Method under test:
     * {@link UserServiceImpl#gatAllUser(Integer, Integer, String, String)}
     */
    @Test
    void testGatAllUser3() {
        // Arrange

        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        ArrayList<User> content = new ArrayList<>();
        content.add(user2);
        content.add(user);
        PageImpl<User> pageImpl = new PageImpl<>(content);
        when(userRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);
        // Act
        UserResponse actualGatAllUserResult = userServiceImpl.gatAllUser(1, 3, "Sort By", "Sort Dir");
        // Assert
        verify(addressClient, atLeast(1)).getAddressByUserId(anyInt());
        verify(userRepository).findAll(isA(Pageable.class));
        assertEquals(0, actualGatAllUserResult.getPageNo());
        assertEquals(1, actualGatAllUserResult.getTotalPages());
        assertEquals(2, actualGatAllUserResult.getPageSize());
        assertEquals(2, actualGatAllUserResult.getContent().size());
        assertEquals(2L, actualGatAllUserResult.getTotalElement());
        assertTrue(actualGatAllUserResult.isLastPage());
    }
    /**
     * Method under test:
     * {@link UserServiceImpl#gatAllUser(Integer, Integer, String, String)}
     */
    @Test
    void testGatAllUser4() {
        // Arrange
        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        ArrayList<User> content = new ArrayList<>();
        content.add(user);
        PageImpl<User> pageImpl = new PageImpl<>(content);
        when(userRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);
        // Act
        UserResponse actualGatAllUserResult = userServiceImpl.gatAllUser(1, 3, "Sort By", "asc");
        // Assert
        verify(addressClient).getAddressByUserId(eq(1));
        verify(userRepository).findAll(isA(Pageable.class));
        assertEquals(0, actualGatAllUserResult.getPageNo());
        assertEquals(1, actualGatAllUserResult.getPageSize());
        assertEquals(1, actualGatAllUserResult.getTotalPages());
        assertEquals(1, actualGatAllUserResult.getContent().size());
        assertEquals(1L, actualGatAllUserResult.getTotalElement());
        assertTrue(actualGatAllUserResult.isLastPage());
    }

    /**
     * Method under test: {@link UserServiceImpl#deleteUser(Integer)}
     */
    @Test
    void testDeleteUser() {
        // Arrange
        doNothing().when(addressClient).deleteAddressByUserId(anyInt());
        doNothing().when(userRepository).delete(Mockito.<User>any());
        when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        // Act
        userServiceImpl.deleteUser(1);
        // Assert that nothing has changed
        verify(addressClient).deleteAddressByUserId(eq(1));
        verify(userRepository).delete(isA(User.class));
        verify(userRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link UserServiceImpl#deleteUser(Integer)}
     */
    @Test
    void testDeleteUser2() {
        // Arrange
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);
        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.deleteUser(1));
        verify(userRepository).findById(eq(1));
    }
}
