package com.blog.app.project.blogappapi.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.blog.app.project.blogappapi.dto.AddressDto;
import com.blog.app.project.blogappapi.dto.CategoryDto;
import com.blog.app.project.blogappapi.dto.PostDto;
import com.blog.app.project.blogappapi.dto.UserDto;
import com.blog.app.project.blogappapi.entity.Category;
import com.blog.app.project.blogappapi.entity.Comment;
import com.blog.app.project.blogappapi.entity.Post;
import com.blog.app.project.blogappapi.entity.User;
import com.blog.app.project.blogappapi.exception.ResourceNotFoundException;
import com.blog.app.project.blogappapi.openfeignclient.AddressClient;
import com.blog.app.project.blogappapi.repository.CategoryRepository;
import com.blog.app.project.blogappapi.repository.PostRepository;
import com.blog.app.project.blogappapi.repository.UserRepository;
import com.blog.app.project.blogappapi.service.impl.PostServiceImpl;
import com.blog.app.project.blogappapi.service.impl.UserServiceImpl;

import feign.FeignException;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

	@InjectMocks
	private PostServiceImpl postServiceImpl;

	@Mock
	private PostRepository postRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private CategoryRepository categoryRepository;
	
	@InjectMocks
	private UserServiceImpl userServiceImpl;

	@Mock
	AddressClient addressClient;

	private User user1;
	private User user2;
	private User user3;
	private CategoryDto categoryDto1;
	private CategoryDto categoryDto2;
	private CategoryDto categoryDto3;
	private Post post1;
	private Post post2;
	private Post post3;
	private Comment comment1;
	private Comment comment2;
	private Comment comment3;
	private Category category;
	private UserDto userDto1;
	private UserDto userDto2;
	private PostDto postDto;
	private AddressDto addressDto1;
	private AddressDto addressDto2;

	@BeforeEach
	public void setUp() {
		categoryDto1 = CategoryDto.builder().categoryId(1).categoryTitle("title1")
				.categoryDescription("this is category1").build();
		categoryDto2 = CategoryDto.builder().categoryId(2).categoryTitle("title2")
				.categoryDescription("this is category2").build();
		categoryDto3 = CategoryDto.builder().categoryId(3).categoryTitle("title3")
				.categoryDescription("this is category3").build();

		userDto1 = UserDto.builder().id(1).name("ram").email("ram@gmail.com").password("ram@123").about("xxxx")
				.addressDto(null).build();
		
		userDto2 = UserDto.builder().id(2).name("ram").email("ram@gmail.com").password("ram@123").about("xxxx")
				.addressDto(null).build();

		category = Category.builder().categoryId(1).categoryTitle("category1").categoryDescription("this is category1")
				.post(Collections.emptyList()).build();

		user1 = User.builder().id(1).name("ram").email("ram@gmail.com").password("ram@123").about("I am user1")
				.post(Collections.emptyList()).comments(Collections.emptySet()).build();
		user2 = User.builder().id(1).name("ram").email("ram@gmail.com").password("ram@123").about("I am user1")
				.post(Collections.emptyList()).comments(Collections.emptySet()).build();
		user3 = User.builder().id(1).name("ram").email("ram@gmail.com").password("ram@123").about("I am user1")
				.post(Collections.emptyList()).comments(Collections.emptySet()).build();

		addressDto1 = new AddressDto(1, "lane1", "lane2", "city", "state");
		addressDto2 = new AddressDto(2, "lane1", "lane2", "city", "state");

		post1 = Post.builder().postId(1).title("Dummy Title").content("Dummy Content").imageName("def.png")
				.addDate(new Date()).category(category).user(user1).comments(Collections.emptySet()).build();

		post2 = Post.builder().postId(2).title("Updated Title").content("Updated Content").imageName("updated.png")
				.addDate(new Date()).category(category).user(user2).comments(Collections.emptySet()).build();

		post3 = Post.builder().postId(3).title("post3").content("this is post 3").imageName("post3.png")
				.addDate(new Date()).category(category).user(user3).comments(Collections.emptySet()).build();

		comment1 = Comment.builder().id(1).content("Dummy Content").post(post1).user(user1).build();
		comment2 = Comment.builder().id(2).content("Dummy Content").post(post2).user(user2).build();
		comment3 = Comment.builder().id(3).content("Dummy Content").post(post3).user(user3).build();

		postDto = PostDto.builder().postId(1).title("dummy title").content("dummy content").imageName("def.png")
				.addedDate(new Date()).category(categoryDto1).user(userDto1).comments(Collections.emptySet()).build();
	}

	@Test
	public void testCreatePost_Success() {
		// Mocking input parameters
		Integer userId = 1;
		Integer categoryId = 1;

		when(userRepository.findById(userId)).thenReturn(Optional.of(user1));
		when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
		when(addressClient.getAddressByUserId(userId)).thenReturn(addressDto1);
		when(postRepository.save(any(Post.class))).thenReturn(post1);

		PostDto createdPostDto = postServiceImpl.createPost(postDto, userId, categoryId);

		Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
		Mockito.verify(categoryRepository, Mockito.times(1)).findById(categoryId);
		Mockito.verify(addressClient, Mockito.times(1)).getAddressByUserId(userId);
		Mockito.verify(postRepository, Mockito.times(1)).save(any(Post.class));

		assertEquals(postDto.getPostId(), createdPostDto.getPostId());
		assertEquals(postDto.getTitle(), createdPostDto.getTitle());
		assertEquals(postDto.getContent(), createdPostDto.getContent());
		assertEquals(postDto.getImageName(), createdPostDto.getImageName());
		assertEquals(addressDto1, createdPostDto.getUser().getAddressDto());
	}

	@Test
	public void testCreatePost_UserNotFound() {
		when(userRepository.findById(1)).thenThrow(new ResourceNotFoundException("User", "UserId", 1));
		assertThrows(ResourceNotFoundException.class, () -> postServiceImpl.createPost(postDto, 1, 1));
		Mockito.verify(userRepository, Mockito.times(1)).findById(1);
	}

	@Test
	public void testCreatePost_CategoryNotFound() {
		when(userRepository.findById(1)).thenReturn(Optional.of(user1));
		when(categoryRepository.findById(1)).thenThrow(new ResourceNotFoundException("Category", "CategoryId", 1));

		assertThrows(ResourceNotFoundException.class, () -> postServiceImpl.createPost(new PostDto(), 1, 1));
		Mockito.verify(userRepository, Mockito.times(1)).findById(1);
		Mockito.verify(categoryRepository, Mockito.times(1)).findById(1);
	}

	@Test
	public void testUpdatePost_Success() {

		Integer postId = 1;
		Set<Comment> comments = new HashSet<>(List.of(comment1, comment2));

		post1.setUser(user1);
		post1.setCategory(category);
		post1.setComments(comments);

		when(postRepository.findById(postId)).thenReturn(Optional.of(post1));
		when(postRepository.save(any(Post.class))).thenReturn(post1);

		AddressDto addressDto = new AddressDto();
		addressDto.setId(1);
		addressDto.setLane1("lane1");
		addressDto.setLane2("lane2");
		addressDto.setCity("city");
		addressDto.setState("state");

		PostDto updatedPostDto = postServiceImpl.updatePost(postDto, postId);
		Mockito.verify(postRepository, Mockito.times(1)).findById(postId);
		Mockito.verify(postRepository, Mockito.times(1)).save(any(Post.class));

		assertEquals(post1.getPostId(), updatedPostDto.getPostId());
		assertEquals(postDto.getTitle(), updatedPostDto.getTitle());
		assertEquals(postDto.getContent(), updatedPostDto.getContent());
		assertEquals(postDto.getImageName(), updatedPostDto.getImageName());
		assertEquals(category.getCategoryId(), updatedPostDto.getCategory().getCategoryId());
		assertEquals(category.getCategoryTitle(), updatedPostDto.getCategory().getCategoryTitle());
		assertEquals(category.getCategoryDescription(), updatedPostDto.getCategory().getCategoryDescription());
		assertEquals(user1.getId(), updatedPostDto.getUser().getId());
		assertEquals(user1.getName(), updatedPostDto.getUser().getName());
		assertEquals(user1.getEmail(), updatedPostDto.getUser().getEmail());
		assertEquals(user1.getPassword(), updatedPostDto.getUser().getPassword());
		assertEquals(user1.getAbout(), updatedPostDto.getUser().getAbout());
		assertEquals(comments.size(), updatedPostDto.getComments().size());
	}

	@Test
	public void testUpdatePost_PostNotFound() {
		when(postRepository.findById(1)).thenThrow(new ResourceNotFoundException("post", "postId", 1));
		assertThrows(ResourceNotFoundException.class, () -> postServiceImpl.updatePost(postDto, 1));
		Mockito.verify(postRepository, Mockito.times(1)).findById(1);
	}

	@Test
	public void testGetPost_succes() throws Exception {
		Integer postId = 1;

		Comment comment1 = new Comment();
		comment1.setId(1);
		comment1.setContent("Comment 1");

		Comment comment2 = new Comment();
		comment2.setId(2);
		comment2.setContent("Comment 2");

		Set<Comment> commentSet = new HashSet<>(List.of(comment1, comment2));

		post1.setUser(user1);
		post1.setCategory(category);
		post1.setComments(commentSet);

		when(postRepository.findById(postId)).thenReturn(Optional.of(post1));
		when(addressClient.getAddressByUserId(user1.getId())).thenReturn(addressDto1);

		PostDto postDto = postServiceImpl.getPostById(postId);
		Mockito.verify(postRepository, Mockito.times(1)).findById(postId);
		Mockito.verify(addressClient, Mockito.times(1)).getAddressByUserId(user1.getId());

		assertEquals(post1.getPostId(), postDto.getPostId());
		assertEquals(post1.getTitle(), postDto.getTitle());
		assertEquals(post1.getContent(), postDto.getContent());
		assertEquals(post1.getImageName(), postDto.getImageName());
		assertEquals(post1.getAddDate(), postDto.getAddedDate());
		assertEquals(category.getCategoryId(), postDto.getCategory().getCategoryId());
		assertEquals(category.getCategoryTitle(), postDto.getCategory().getCategoryTitle());
		assertEquals(category.getCategoryDescription(), postDto.getCategory().getCategoryDescription());
		assertEquals(user1.getId(), postDto.getUser().getId());
		assertEquals(user1.getName(), postDto.getUser().getName());
		assertEquals(user1.getEmail(), postDto.getUser().getEmail());
		assertEquals(user1.getPassword(), postDto.getUser().getPassword());
		assertEquals(user1.getAbout(), postDto.getUser().getAbout());
		assertEquals(addressDto1, postDto.getUser().getAddressDto());
		assertEquals(commentSet.size(), postDto.getComments().size());
	}

	@Test
	public void testDeletePost_success() throws Exception {
		int postId = 1;

		Mockito.when(postRepository.findById(postId)).thenReturn(Optional.of(post1));
		postServiceImpl.deletePost(postId);
		Mockito.verify(postRepository, Mockito.times(1)).findById(postId);
	}

	@Test
	public void testDeletepost_NotFound() throws Exception {
		int postId = 10;
		Mockito.when(postRepository.findById(postId))
				.thenThrow(new ResourceNotFoundException("post", "postId", postId));

		assertThrows(ResourceNotFoundException.class, () -> postServiceImpl.deletePost(postId));
		Mockito.verify(postRepository, Mockito.times(1)).findById(postId);
	}

	@Test
	public void testGetPostById_UserAddressNotFound() {
		// Mocking input parameters
		Integer postId = 1;
		post1.setUser(user1);

		when(postRepository.findById(postId)).thenReturn(Optional.of(post1));
		when(addressClient.getAddressByUserId(user1.getId())).thenThrow(FeignException.NotFound.class);

		PostDto postDto = postServiceImpl.getPostById(postId);
		Mockito.verify(postRepository, Mockito.times(1)).findById(postId);
		Mockito.verify(addressClient, Mockito.times(1)).getAddressByUserId(user1.getId());

		assertEquals(null, postDto.getUser().getAddressDto());
	}

	@Test
	public void testGetPostById_NotFound() throws Exception {

		int postId = 1;
		Mockito.when(postRepository.findById(postId))
				.thenThrow(new ResourceNotFoundException("post", "postId", postId));
		assertThrows(ResourceNotFoundException.class, () -> postServiceImpl.getPostById(postId));
		Mockito.verify(postRepository, Mockito.times(1)).findById(postId);
	}
}
