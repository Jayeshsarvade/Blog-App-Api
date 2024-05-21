package com.blog.app.project.blogappapi.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

import com.blog.app.project.blogappapi.entity.Category;
import com.blog.app.project.blogappapi.entity.Comment;
import com.blog.app.project.blogappapi.entity.Post;
import com.blog.app.project.blogappapi.entity.User;
import com.blog.app.project.blogappapi.repository.CategoryRepository;
import com.blog.app.project.blogappapi.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc  // provided by spring boot used in integration testing for web application.
public class CategoryControllerTest {
	
	ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();  // convert java object into Json String 

    @Autowired
    private MockMvc mockMvc; // use this to test controller endpoints

    @MockBean
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @MockBean
    private CategoryRepository categoryRepository;
    


    private User user1;
    private User user2;
    private User user3;
    private Post post1;
    private Post post2;
    private Post post3;
    private Comment comment1;
    private Comment comment2;
    private Comment comment3;
    private Category category;
    private Category category1;
    private Category category2;

    @BeforeEach
    public void setUp() {
    	System.out.println("start");
        category = Category.builder()
                .categoryId(1)
                .categoryTitle("category1")
                .categoryDescription("this is category1")
                .post(Collections.emptyList()).build();
        category1 = Category.builder()
                .categoryId(1)
                .categoryTitle("category1")
                .categoryDescription("this is category1")
                .post(Collections.emptyList()).build();
        category2 = Category.builder()
                .categoryId(1)
                .categoryTitle("category1")
                .categoryDescription("this is category1")
                .post(Collections.emptyList()).build();

        user1 = User.builder().id(1).name("ram").email("ram@gmail.com").password("ram@123")
                .about("I am user1").post(Collections.emptyList()).comments(Collections.emptySet()).build();
        user2 = User.builder().id(1).name("ram").email("ram@gmail.com").password("ram@123")
                .about("I am user1").post(Collections.emptyList()).comments(Collections.emptySet()).build();
        user3 = User.builder().id(1).name("ram").email("ram@gmail.com").password("ram@123")
                .about("I am user1").post(Collections.emptyList()).comments(Collections.emptySet()).build();

        post1 = Post.builder().postId(1)
                .title("post1").content("this is post 1")
                .imageName("post1.png").addDate(new Date())
                .category(category)
                .user(user1).comments(Collections.emptySet())
                .build();

        post2 = Post.builder().postId(2)
                .title("post2").content("this is post 2")
                .imageName("post2.png").addDate(new Date())
                .category(category)
                .user(user2).comments(Collections.emptySet())
                .build();

        post3 = Post.builder().postId(3)
                .title("post3").content("this is post 3")
                .imageName("post3.png").addDate(new Date())
                .category(category)
                .user(user3).comments(Collections.emptySet())
                .build();

        comment1 = Comment.builder().id(1)
                .content("Dummy Content").post(post1).user(user1).build();
        comment1 = Comment.builder().id(2)
                .content("Dummy Content").post(post2).user(user2).build();
        comment1 = Comment.builder().id(3)
                .content("Dummy Content").post(post3).user(user3).build();
    }
	
	@Test
	public void testcreateCategory() throws Exception{
		when(categoryRepository.save(category)).thenReturn(category);
		
		String content = objectWriter.writeValueAsString(category);  // convert java object into Json String 

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/api/categories/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andReturn();
	} 
    
	@Test
	public void testUpdateCategory() throws Exception{
		
		Category updatedCategory = Category.builder()
                .categoryId(1)
                .categoryTitle("category1")
                .categoryDescription("this is category1")
                .post(Collections.emptyList()).build();
		
		when(categoryRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(category));
		when(categoryRepository.save(updatedCategory)).thenReturn(updatedCategory);
		
		 String updatedContent = objectWriter.writeValueAsString(updatedCategory);

	        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
	                .put("/api/categories/1")
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON)
	                .content(updatedContent);

	        mockMvc.perform(mockRequest)
	                .andExpect(status().isOk())
	                .andReturn();
	}
	
	@Test
	public void testDeleteCategory()throws Exception{
		when(categoryRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(category));
		
		mockMvc.perform(MockMvcRequestBuilders
				.delete("/api/categories/1")
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk());
	}
	
	@Test
	public void testGetCategoryById() throws Exception{
		int categoryId = 1;
		when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/api/categories/1")
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk());
		
	}
	
	@Test
	public void testGetAllCategory() throws Exception{
		
		List<Category> categories = new ArrayList<>();
		categories.add(category);
		categories.add(category1);
		categories.add(category2);
		
		when(categoryRepository.findAll()).thenReturn(categories);
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/api/categories/")
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk());
	}

	

	
	
}
