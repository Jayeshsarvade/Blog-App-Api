package com.blog.app.project.blogappapi.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import com.blog.app.project.blogappapi.dto.AddressDto;
import com.blog.app.project.blogappapi.dto.CategoryDto;
import com.blog.app.project.blogappapi.dto.UserDto;
import com.blog.app.project.blogappapi.entity.Category;
import com.blog.app.project.blogappapi.entity.Comment;
import com.blog.app.project.blogappapi.entity.Post;
import com.blog.app.project.blogappapi.entity.User;
import com.blog.app.project.blogappapi.exception.ResourceNotFoundException;
import com.blog.app.project.blogappapi.openfeignclient.AddressClient;
import com.blog.app.project.blogappapi.payload.UserResponse;
import com.blog.app.project.blogappapi.repository.UserRepository;
import com.blog.app.project.blogappapi.service.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@InjectMocks // eg. dependency injection
	private UserServiceImpl userServiceImpl;

	@Mock
	private UserRepository userRepository;

	@Mock
	private AddressClient addressClient;

	@Mock
	private Logger logger;

	private User user1;
	private User user2;
	private User user3;
	private UserDto userDto1;
	private UserDto userDto2;
	private CategoryDto categoryDto1;
	private CategoryDto categoryDto2;
	private CategoryDto categoryDto3;
	private Post post1;
	private Post post2;
	private Post post3;
	private Comment comment1;
	private Comment comment2;
	private Comment comment3;
	private UserResponse userResponse;
	private AddressDto addressDto;
	private AddressDto addressDto1;

	@BeforeEach
	public void setUp() {
		addressDto = AddressDto.builder().id(1).lane1("radha nagar").lane2("gulab nagar").city("pune")
				.state("maharashtra").build();

		addressDto1 = AddressDto.builder().id(1).lane1("radha nagar").lane2("gulab nagar").city("pune")
				.state("maharashtra").build();

		categoryDto1 = CategoryDto.builder().categoryId(1).categoryTitle("title1")
				.categoryDescription("this is category1").build();
		categoryDto2 = CategoryDto.builder().categoryId(2).categoryTitle("title2")
				.categoryDescription("this is category2").build();
		categoryDto3 = CategoryDto.builder().categoryId(3).categoryTitle("title3")
				.categoryDescription("this is category3").build();

		Category category = Category.builder().categoryId(1).categoryTitle("category1")
				.categoryDescription("this is category1").post(Collections.emptyList()).build();

		user1 = User.builder().id(1).name("ram").email("ram@gmail.com").password("ram@123").about("xxxx")
				.post(Collections.emptyList()).comments(Collections.emptySet()).build();
		user2 = User.builder().id(1).name("ram").email("ram@gmail.com").password("ram@123").about("I am user1")
				.post(Collections.emptyList()).comments(Collections.emptySet()).build();
		user3 = User.builder().id(1).name("ram").email("ram@gmail.com").password("ram@123").about("I am user1")
				.post(Collections.emptyList()).comments(Collections.emptySet()).build();

		post1 = Post.builder().postId(1).title("post1").content("this is post 1").imageName("post1.png")
				.addDate(new Date()).category(category).user(user1).comments(Collections.emptySet()).build();

		post2 = Post.builder().postId(2).title("post2").content("this is post 2").imageName("post2.png")
				.addDate(new Date()).category(category).user(user2).comments(Collections.emptySet()).build();

		post3 = Post.builder().postId(3).title("post3").content("this is post 3").imageName("post3.png")
				.addDate(new Date()).category(category).user(user3).comments(Collections.emptySet()).build();

		comment1 = Comment.builder().id(1).content("Dummy Content").post(post1).user(user1).build();
		comment2 = Comment.builder().id(2).content("Dummy Content").post(post2).user(user2).build();
		comment3 = Comment.builder().id(3).content("Dummy Content").post(post3).user(user3).build();

		userDto1 = UserDto.builder().id(1).name("ram").email("ram@gmail.com").password("ram@123").about("xxxx")
				.addressDto(null).build();

		userDto2 = UserDto.builder().id(2).name("ram").email("ram@gmail.com").password("ram@123").about("xxxx")
				.addressDto(null).build();
	}

	@Test
	public void testCreateUser_Success() {
		User savedUser = new User();
		savedUser.setId(userDto1.getId());
		savedUser.setName(userDto1.getName());
		savedUser.setEmail(userDto1.getEmail());
		savedUser.setPassword(userDto1.getPassword());
		savedUser.setAbout(userDto1.getAbout());

		when(userRepository.save(any(User.class))).thenReturn(savedUser);
		UserDto returnedUserDto = userServiceImpl.createUser(userDto1);

		Mockito.verify(userRepository, Mockito.times(1)).save(any(User.class));

		assertEquals(userDto1.getId(), returnedUserDto.getId());
		assertEquals(userDto1.getName(), returnedUserDto.getName());
		assertEquals(userDto1.getEmail(), returnedUserDto.getEmail());
		assertEquals(userDto1.getPassword(), returnedUserDto.getPassword());
		assertEquals(userDto1.getAbout(), returnedUserDto.getAbout());
	}

	@Test
	public void testCreateUser_SaveFailed() {

		when(userRepository.save(user1)).thenThrow(RuntimeException.class);
		assertThrows(RuntimeException.class, () -> userServiceImpl.createUser(userDto1));
		Mockito.verify(userRepository, Mockito.times(1)).save(any(User.class));
	}

	@Test
	public void testUpdateUser_Success() {
		Integer userId = 1;

		when(userRepository.findById(userId)).thenReturn(Optional.of(user1));
		when(userRepository.save(any(User.class))).thenReturn(user1);

		when(addressClient.getAddressByUserId(userId)).thenReturn(addressDto);

		UserDto updatedUserDto = userServiceImpl.updateUSer(userDto1, userId);

		Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
		Mockito.verify(userRepository, Mockito.times(1)).save(any(User.class));
		Mockito.verify(addressClient, Mockito.times(1)).getAddressByUserId(userId);

		assertEquals(userDto1.getId(), updatedUserDto.getId());
		assertEquals(userDto1.getName(), updatedUserDto.getName());
		assertEquals(userDto1.getEmail(), updatedUserDto.getEmail());
		assertEquals(userDto1.getPassword(), updatedUserDto.getPassword());
		assertEquals(userDto1.getAbout(), updatedUserDto.getAbout());
		assertEquals(addressDto, updatedUserDto.getAddressDto());
	}

	@Test
	public void testUpdateUser_UserNotFound() {
		Integer userId = 1;

		when(userRepository.findById(userId)).thenThrow(new ResourceNotFoundException("User", "UserId", userId));
		assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.updateUSer(userDto1, userId));
		Mockito.verify(userRepository, Mockito.times(1)).findById(userId);

	}

	@Test
	public void testGetUser_succes() throws Exception {

		Integer userId = 1;

		UserDto userDto = new UserDto();
		userDto.setId(user1.getId());
		userDto.setName(user1.getName());
		userDto.setEmail(user1.getEmail());
		userDto.setPassword(user1.getPassword());
		userDto.setAbout(user1.getAbout());

		when(userRepository.findById(userId)).thenReturn(Optional.of(user1));
		when(addressClient.getAddressByUserId(userId)).thenReturn(addressDto);

		UserDto returnedUserDto = userServiceImpl.getUser(userId);

		Mockito.verify(userRepository).findById(userId);
		Mockito.verify(addressClient).getAddressByUserId(userId);

		assertEquals(userDto.getId(), returnedUserDto.getId());
		assertEquals(userDto.getName(), returnedUserDto.getName());
		assertEquals(userDto.getEmail(), returnedUserDto.getEmail());
		assertEquals(userDto.getPassword(), returnedUserDto.getPassword());
		assertEquals(userDto.getAbout(), returnedUserDto.getAbout());
		assertEquals(addressDto, returnedUserDto.getAddressDto());
		assertEquals(addressDto, returnedUserDto.getAddressDto());
	}

	@Test
	public void testGetUserById_NotFound() throws Exception {

		int id = 1;
		Mockito.when(userRepository.findById(id)).thenThrow(new ResourceNotFoundException("User", "UserId", id));
		assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.getUser(id));
		Mockito.verify(userRepository, Mockito.times(1)).findById(id);
	}

	@Test
	public void testDeleteUser_success() throws Exception {
		int userId = 1;

		Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user1));
		userServiceImpl.deleteUser(userId);
		Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
		Mockito.verify(userRepository, Mockito.times(1)).delete(user1);
	}

	@Test
	public void testDeleteUser_NotFound() throws Exception {
		int userId = 1;
		Mockito.when(userRepository.findById(userId))
				.thenThrow(new ResourceNotFoundException("User", "UserId", userId));
		assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.deleteUser(userId));
		Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
	}

}
