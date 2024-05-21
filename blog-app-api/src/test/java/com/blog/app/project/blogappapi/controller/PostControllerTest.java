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
import com.blog.app.project.blogappapi.repository.PostRepository;
import com.blog.app.project.blogappapi.service.FileService;
import com.blog.app.project.blogappapi.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(PostController.class)
@AutoConfigureMockMvc
public class PostControllerTest {

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private FileService fileService;

    @InjectMocks
    private PostController postController;

    @MockBean
    private PostRepository postRepository;

    private User user1;
    private User user2;
    private User user3;
    private Post post1;
    private Post post2;
    private Post post3;
    private Comment comment1;
    private Comment comment2;
    private Comment comment3;
    Category category;

    @BeforeEach
    public void setUp() {
    	System.out.println("start");
        category = Category.builder()
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
    public void testCreatePost_success() throws Exception{
        Set<Comment> comments = new HashSet<>(Arrays.asList(comment1,comment2,comment3));

        Post post = new Post(1,"post","dummy post","def.png",
                new Date(),category,user1,comments);

        Mockito.when(postRepository.save(post)).thenReturn(post);

        String content = objectWriter.writeValueAsString(post);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/api/user/12/category/3/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andReturn();
    }
    
    @Test
    public void testGetPostsByUser_success() throws Exception{
        List<Post> posts = new ArrayList<>(Arrays.asList(post1,post2,post3));
    Mockito.when(postRepository.findByUser(user1)).thenReturn(posts);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/user/2/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testGetPostsByCategory_success() throws Exception{
        List<Post> posts = new ArrayList<>(Arrays.asList(post1,post2,post3));
        Mockito.when(postRepository.findByCategory(category)).thenReturn(posts);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/category/2/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testGetPostById_success() throws Exception{
        Mockito.when(postRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(post1));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/posts/12")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }
    
    @Test
    public void testGetAllPost_success() throws Exception{
        List<Post> posts = new ArrayList<>(Arrays.asList(post1,post2,post3));

        Mockito.when(postRepository.findAll()).thenReturn(posts);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/posts/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
    }
    
    @Test
    public void testDeletePost_success() throws Exception {
        Mockito.when(postRepository.findById(post1.getPostId())).thenReturn(Optional.of(post1));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/posts/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdatePost_success() throws Exception{
        Set<Comment> comments = new HashSet<>(Arrays.asList(comment1,comment2,comment3));

        Post updatedPost = new Post(1,"post","dummy post","def.png",
                new Date(),category,user1,comments);

        Mockito.when(postRepository.findById(post1.getPostId())).thenReturn(Optional.of(post1));
        Mockito.when(postRepository.save(updatedPost)).thenReturn(updatedPost);

        String updatedContent = objectWriter.writeValueAsString(updatedPost);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .put("/api/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(updatedContent);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testSearchPostsByKeyword_success() throws Exception{
        List<Post> posts = new ArrayList<>(Arrays.asList(post1,post2,post3));
        Mockito.when(postRepository.findByTitleContaining(Mockito.anyString())).thenReturn(posts);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/posts/search/sadj")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
    }
}
