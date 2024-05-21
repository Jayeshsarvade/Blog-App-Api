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

import com.blog.app.project.blogappapi.dto.CategoryDto;
import com.blog.app.project.blogappapi.dto.CommentDto;
import com.blog.app.project.blogappapi.entity.Category;
import com.blog.app.project.blogappapi.entity.Comment;
import com.blog.app.project.blogappapi.entity.Post;
import com.blog.app.project.blogappapi.entity.User;
import com.blog.app.project.blogappapi.exception.ResourceNotFoundException;
import com.blog.app.project.blogappapi.payload.UserResponse;
import com.blog.app.project.blogappapi.repository.CommentRepository;
import com.blog.app.project.blogappapi.repository.PostRepository;
import com.blog.app.project.blogappapi.repository.UserRepository;
import com.blog.app.project.blogappapi.service.impl.CommentServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CommentsServiceTest {

	  @InjectMocks
	    private CommentServiceImpl commentServiceImpl;


	    @Mock 
	    private CommentRepository commentRepository;
	    
	    @Mock
	    private UserRepository userRepository;
	    
	    @Mock
	    private PostRepository postRepository;
	    

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
	    private UserResponse userResponse;
	    private Category category;
	    private CommentDto commentDto1;

	    @BeforeEach
	    public void setUp() {
	        categoryDto1 = CategoryDto.builder()
	                .categoryId(1).categoryTitle("title1")
	                .categoryDescription("this is category1").build();
	        categoryDto2 = CategoryDto.builder()
	                .categoryId(2).categoryTitle("title2")
	                .categoryDescription("this is category2").build();
	        categoryDto3 = CategoryDto.builder()
	                .categoryId(3).categoryTitle("title3")
	                .categoryDescription("this is category3").build();


	         category= Category.builder()
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
	                .content("dummy content").post(post1).user(user1).build();
	        comment2 = Comment.builder().id(2)
	                .content("Dummy Content").post(post2).user(user2).build();
	        comment3 = Comment.builder().id(3)
	                .content("Dummy Content").post(post3).user(user3).build();
	        
	        commentDto1 = CommentDto.builder().id(1).content("dummy content").build();
			
	    }

	    
	    @Test
		public void testDeleteComment_success() throws Exception {
			int commentId = 1;

			Mockito.when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment1));
			commentServiceImpl.deleteComment(commentId);
			Mockito.verify(commentRepository, Mockito.times(1)).findById(commentId);
		}

		@Test
		public void testDeleteComment_NotFound() throws Exception {
			int commentId = 10;
			Mockito.when(commentRepository.findById(commentId))
					.thenThrow(new ResourceNotFoundException("comment", "commentId", commentId));

			assertThrows(ResourceNotFoundException.class, () -> commentServiceImpl.deleteComment(commentId));
			Mockito.verify(commentRepository, Mockito.times(1)).findById(commentId);
		}
		
		@Test
		public void testGetComment_succes()throws Exception{
			
	        Integer commentId = 1;

	        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment1));
	        CommentDto commentDto = commentServiceImpl.getComment(commentId);

	        assertEquals(comment1.getId(), commentDto.getId());
	        assertEquals(comment1.getContent(), commentDto.getContent());
			Mockito.verify(commentRepository, Mockito.times(1)).findById(commentId);

		}
		
		@Test
		public void testGetCommentById_NotFound() throws Exception {

			int commentId = 1;
			Mockito.when(commentRepository.findById(commentId)).thenThrow(new ResourceNotFoundException("comment", "commentId", commentId));
			assertThrows(ResourceNotFoundException.class, () -> commentServiceImpl.getComment(commentId));
			Mockito.verify(commentRepository, Mockito.times(1)).findById(commentId);

		}
		
		@Test
		public void testCreateComment_success()throws Exception{
	        Integer userId = 1;
	        Integer postId = 1;

	        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));
	        when(postRepository.findById(postId)).thenReturn(Optional.of(post1));
	        when(commentRepository.save(any(Comment.class))).thenReturn(comment1);

	        CommentDto createdCommentDto = commentServiceImpl.createComment(commentDto1, userId, postId);
	        
			Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
			Mockito.verify(postRepository, Mockito.times(1)).findById(postId);
			Mockito.verify(commentRepository, Mockito.times(1)).save(any(Comment.class));

	        assertEquals(commentDto1.getId(), createdCommentDto.getId());
	        assertEquals(commentDto1.getContent(), createdCommentDto.getContent());
		}
		
		@Test
	    public void testCreateComment_UserNotFound() {
			
	        when(userRepository.findById(1)).thenReturn(Optional.empty());
	        assertThrows(ResourceNotFoundException.class, () -> commentServiceImpl.createComment(new CommentDto(), 1, 1));
			Mockito.verify(userRepository, Mockito.times(1)).findById(1);

	    }
		
		@Test
	    public void testCreateComment_PostNotFound() {
	        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
	        when(postRepository.findById(1)).thenReturn(Optional.empty());
	        assertThrows(ResourceNotFoundException.class, () -> commentServiceImpl.createComment(commentDto1, 1, 1));
			Mockito.verify(userRepository, Mockito.times(1)).findById(1);
			Mockito.verify(postRepository, Mockito.times(1)).findById(1);

	    }
		
		
		@Test
	    public void testUpdateComment_Success() {
	        int commentId = 1;
	        String updatedContent = "Updated content";

	        CommentDto updatedCommentDto = new CommentDto();
	        updatedCommentDto.setId(commentId);
	        updatedCommentDto.setContent(updatedContent);

	        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment1));
	        when(commentRepository.save(comment1)).thenReturn(comment1);
	        CommentDto returnedCommentDto = commentServiceImpl.updateComment(commentId, updatedCommentDto);
	        
			Mockito.verify(commentRepository, Mockito.times(1)).findById(1);
			Mockito.verify(commentRepository, Mockito.times(1)).save(comment1);

			assertEquals(updatedContent, returnedCommentDto.getContent());
	    }
		
		@Test
		public void testUpdateComment_CommentNotFound()throws Exception{
			int commentId = 1;
			
			when(commentRepository.findById(1)).thenThrow(new ResourceNotFoundException("Comment","CommentId",commentId));
			assertThrows(ResourceNotFoundException.class, ()->commentServiceImpl.updateComment(commentId, commentDto1));
			Mockito.verify(commentRepository, Mockito.times(1)).findById(1);
		}
}


















