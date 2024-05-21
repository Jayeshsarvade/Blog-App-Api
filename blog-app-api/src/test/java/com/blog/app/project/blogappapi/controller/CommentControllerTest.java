package com.blog.app.project.blogappapi.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
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

import com.blog.app.project.blogappapi.entity.Category;
import com.blog.app.project.blogappapi.entity.Comment;
import com.blog.app.project.blogappapi.entity.Post;
import com.blog.app.project.blogappapi.entity.User;
import com.blog.app.project.blogappapi.repository.CommentRepository;
import com.blog.app.project.blogappapi.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc
public class CommentControllerTest {

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    @MockBean
    private CommentRepository commentRepository;


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
        comment1 = Comment.builder().id(2)
                .content("Dummy Content").post(post2).user(user2).build();
        comment1 = Comment.builder().id(3)
                .content("Dummy Content").post(post3).user(user3).build();

    }
    
    @Test
    public void testCreateComment_success() throws Exception{
    Comment comment = new Comment(1,"content",post1,user1);

    Mockito.when(commentRepository.findById(comment1.getId())).thenReturn(Optional.of(comment1));
    Mockito.when(commentRepository.save(comment)).thenReturn(comment);

        String content = objectWriter.writeValueAsString(comment);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/api/user/12/post/23/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andReturn();
    }
    

    @Test
    public void testDeleteComment_success() throws Exception{
    Mockito.when(commentRepository.findById(comment1.getId())).thenReturn(Optional.of(comment1));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/comment/123")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }


    @Test
   public void testGetAllComments_success() throws Exception{

        List<Comment> comments = new ArrayList<>(Arrays.asList(comment1,comment2, comment3));
        Mockito.when(commentRepository.findAll()).thenReturn(comments);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testGetCommentById_success() throws Exception{

        Mockito.when(commentRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(comment1));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/comment/123")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }
    

    @Test
    public void testUpdateComment_success()throws Exception{
    	
    	Comment updatedComment = Comment.builder().id(1).content("dummy content").build();
    	
    	int commentId = 1;
    	Mockito.when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment1));
    	when(commentRepository.save(updatedComment)).thenReturn(updatedComment);
    	
    	String writeValueAsString = objectWriter.writeValueAsString(comment1);
    	
    	   MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                   .put("/api/comment/1")
                   .contentType(MediaType.APPLICATION_JSON)
                   .accept(MediaType.APPLICATION_JSON)
                   .content(writeValueAsString);
    	   
    	   mockMvc.perform(mockRequest)
           .andExpect(status().isOk())
           .andReturn();
    }

    }
