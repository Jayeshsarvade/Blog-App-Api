package com.blog.app.project.blogappapi.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
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
import com.blog.app.project.blogappapi.repository.UserRepository;
import com.blog.app.project.blogappapi.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @MockBean
    private UserRepository userRepository;

    private User user1;
    private User user2;
    private User user3;
    private Post post1;
    private Post post2;
    private Post post3;
    private Comment comment1;
    private Comment comment2;
    private Comment comment3;

    @BeforeEach
    public void setUp() {
    	System.out.println("start");
        Category category = Category.builder()
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
        comment2 = Comment.builder().id(2)
                .content("Dummy Content").post(post2).user(user2).build();
        comment3 = Comment.builder().id(3)
                .content("Dummy Content").post(post3).user(user3).build();

    }

    @Test
    public void testGetAllUsersTest() throws Exception {
        List<User> users = new ArrayList<>(Arrays.asList(user1, user2, user3));
        Mockito.when(userRepository.findAll()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testGetUserById() throws Exception {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/users/123")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

    }

    @Test
    public void testCreateUser_success() throws Exception {
        List<Post> posts = new ArrayList<>(Arrays.asList(post1, post2, post3));
        Set<Comment> comments = new HashSet<>(Arrays.asList(comment1, comment2, comment3));

        User user = User.builder()
                .id(1).name("user").email("user@gmail.com")
                .password("user@123").about("demo user")
                .post(posts).comments(comments).build();

        Mockito.when(userRepository.save(user)).thenReturn(user);

        String content = objectWriter.writeValueAsString(user);  // it takes object as a input and returns Json

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/api/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    public void testUpdateUser_success() throws Exception {

        List<Post> posts = new ArrayList<>(Arrays.asList(post1, post2, post3));
        Set<Comment> comments = new HashSet<>(Arrays.asList(comment1, comment2, comment3));

        User updatedRecord = User.builder()
                .id(1).name("updated name").email("UN@gmail.com")
                .password("userUpdate@123").about("updated user")
                .post(posts).comments(comments).build();

        Mockito.when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.save(updatedRecord)).thenReturn(updatedRecord);

        String updatedContent = objectWriter.writeValueAsString(updatedRecord);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
        		.put("/api/users/123")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(updatedContent);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testDeleteUser_success() throws Exception {
        Mockito.when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/users/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}

