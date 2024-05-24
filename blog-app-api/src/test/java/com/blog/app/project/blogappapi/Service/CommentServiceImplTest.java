package com.blog.app.project.blogappapi.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blog.app.project.blogappapi.dto.CommentDto;
import com.blog.app.project.blogappapi.entity.Category;
import com.blog.app.project.blogappapi.entity.Comment;
import com.blog.app.project.blogappapi.entity.Post;
import com.blog.app.project.blogappapi.entity.User;
import com.blog.app.project.blogappapi.exception.ResourceNotFoundException;
import com.blog.app.project.blogappapi.payload.CommentResponse;
import com.blog.app.project.blogappapi.repository.CommentRepository;
import com.blog.app.project.blogappapi.repository.PostRepository;
import com.blog.app.project.blogappapi.repository.UserRepository;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import com.blog.app.project.blogappapi.service.impl.CommentServiceImpl;
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

@ContextConfiguration(classes = {CommentServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class CommentServiceImplTest {
    @MockBean
    private CommentRepository commentRepository;

    @Autowired
    private CommentServiceImpl commentServiceImpl;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private UserRepository userRepository;

    private Category category;
    private User user;
    private Post post;
    private User user2;
    private Comment comment;
    private Comment comment2;
    private Category category2;
    private User user3;
    private Post post2;
    private User user4;

    @BeforeEach
    public void setUp() {
        category = new Category();
        category.setCategoryDescription("Category Description");
        category.setCategoryId(1);
        category.setCategoryTitle("Dr");
        category.setPost(new ArrayList<>());

        user = new User();
        user.setAbout("About");
        user.setComments(new HashSet<>());
        user.setEmail("jane.doe@example.org");
        user.setId(1);
        user.setName("Name");
        user.setPassword("iloveyou");
        user.setPost(new ArrayList<>());

        post = new Post();
        post.setAddDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        post.setCategory(category);
        post.setComments(new HashSet<>());
        post.setContent("Not all who wander are lost");
        post.setImageName("Image Name");
        post.setPostId(1);
        post.setTitle("Dr");
        post.setUser(user);

        user2 = new User();
        user2.setAbout("About");
        user2.setComments(new HashSet<>());
        user2.setEmail("jane.doe@example.org");
        user2.setId(1);
        user2.setName("Name");
        user2.setPassword("iloveyou");
        user2.setPost(new ArrayList<>());

        comment = new Comment();
        comment.setContent("Not all who wander are lost");
        comment.setId(1);
        comment.setPost(post);
        comment.setUser(user2);

        comment2 = new Comment();
        comment2.setContent("Fetching all comments with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}");
        comment2.setId(2);
        comment2.setPost(post2);
        comment2.setUser(user4);

        category2 = new Category();
        category2.setCategoryDescription("Category Description");
        category2.setCategoryId(1);
        category2.setCategoryTitle("Dr");
        category2.setPost(new ArrayList<>());

        user3 = new User();
        user3.setAbout("About");
        user3.setComments(new HashSet<>());
        user3.setEmail("jane.doe@example.org");
        user3.setId(1);
        user3.setName("Name");
        user3.setPassword("iloveyou");
        user3.setPost(new ArrayList<>());

        post2 = new Post();
        post2.setAddDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        post2.setCategory(category2);
        post2.setComments(new HashSet<>());
        post2.setContent("Not all who wander are lost");
        post2.setImageName("Image Name");
        post2.setPostId(1);
        post2.setTitle("Dr");
        post2.setUser(user3);

        user4 = new User();
        user4.setAbout("About");
        user4.setComments(new HashSet<>());
        user4.setEmail("jane.doe@example.org");
        user4.setId(1);
        user4.setName("Name");
        user4.setPassword("iloveyou");
        user4.setPost(new ArrayList<>());
    }

    /**
     * Method under test:
     * {@link CommentServiceImpl#createComment(CommentDto, Integer, Integer)}
     */
    @Test
    void testCreateComment() {
        // Arrange
        when(commentRepository.save(Mockito.<Comment>any())).thenReturn(comment);
        when(postRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(post2));
        when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user4));
        // Act
        CommentDto actualCreateCommentResult = commentServiceImpl.createComment(new CommentDto(), 1, 1);

        // Assert
        verify(postRepository).findById(eq(1));
        verify(userRepository).findById(eq(1));
        verify(commentRepository).save(isA(Comment.class));
        assertNull(actualCreateCommentResult.getId());
        assertNull(actualCreateCommentResult.getContent());
    }

    /**
     * Method under test:
     * {@link CommentServiceImpl#createComment(CommentDto, Integer, Integer)}
     */
    @Test
    void testCreateComment2() {
        // Arrange
        when(commentRepository.save(Mockito.<Comment>any()))
                .thenThrow(new ResourceNotFoundException("creating comment: {} {} {}", "creating comment: {} {} {}", 42L));
        when(postRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(post));
        when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user2));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> commentServiceImpl.createComment(new CommentDto(), 1, 1));
        verify(postRepository).findById(eq(1));
        verify(userRepository).findById(eq(1));
        verify(commentRepository).save(isA(Comment.class));
    }

    /**
     * Method under test:
     * {@link CommentServiceImpl#createComment(CommentDto, Integer, Integer)}
     */
    @Test
    void testCreateComment3() {
        // Arrange
        when(postRepository.findById(Mockito.<Integer>any()))
                .thenThrow(new ResourceNotFoundException("creating comment: {} {} {}", "creating comment: {} {} {}", 42L));
        when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));

        CommentDto commentDto = CommentDto.builder().content("Not all who wander are lost").id(1).build();

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> commentServiceImpl.createComment(commentDto, 1, 1));
        verify(postRepository).findById(eq(1));
        verify(userRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link CommentServiceImpl#deleteComment(Integer)}
     */
    @Test
    void testDeleteComment() {
        // Arrange
        doNothing().when(commentRepository).delete(Mockito.<Comment>any());
        when(commentRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(comment));

        // Act
        commentServiceImpl.deleteComment(1);

        // Assert that nothing has changed
        verify(commentRepository).delete(isA(Comment.class));
        verify(commentRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link CommentServiceImpl#deleteComment(Integer)}
     */
    @Test
    void testDeleteComment2() {
        // Arrange
        doThrow(new ResourceNotFoundException("deleting comment: {}", "deleting comment: {}", 42L)).when(commentRepository)
                .delete(Mockito.<Comment>any());
        when(commentRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(comment));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> commentServiceImpl.deleteComment(1));
        verify(commentRepository).delete(isA(Comment.class));
        verify(commentRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link CommentServiceImpl#deleteComment(Integer)}
     */
    @Test
    void testDeleteComment3() {
        // Arrange
        Optional<Comment> emptyResult = Optional.empty();
        when(commentRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> commentServiceImpl.deleteComment(1));
        verify(commentRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link CommentServiceImpl#getComment(Integer)}
     */
    @Test
    void testGetComment() {
        // Arrange
        when(commentRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(comment));
        // Act
        CommentDto actualComment = commentServiceImpl.getComment(1);
        // Assert
        verify(commentRepository).findById(eq(1));
        assertEquals("Not all who wander are lost", actualComment.getContent());
        assertEquals(1, actualComment.getId().intValue());
    }

    /**
     * Method under test: {@link CommentServiceImpl#getComment(Integer)}
     */
    @Test
    void testGetComment2() {
        // Arrange
        Optional<Comment> emptyResult = Optional.empty();
        when(commentRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> commentServiceImpl.getComment(1));
        verify(commentRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link CommentServiceImpl#getComment(Integer)}
     */
    @Test
    void testGetComment3() {
        // Arrange
        when(commentRepository.findById(Mockito.<Integer>any()))
                .thenThrow(new ResourceNotFoundException("getting comment: {}", "getting comment: {}", 42L));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> commentServiceImpl.getComment(1));
        verify(commentRepository).findById(eq(1));
    }

    /**
     * Method under test:
     * {@link CommentServiceImpl#getAllComment(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllComment() {
        // Arrange
        ArrayList<Comment> commentList = new ArrayList<>();
        when(commentRepository.findAll()).thenReturn(commentList);
        when(commentRepository.findAll(Mockito.<Pageable>any())).thenReturn(new PageImpl<>(new ArrayList<>()));

        // Act
        CommentResponse actualAllComment = commentServiceImpl.getAllComment(1, 3, "Sort By", "Sort Dir");

        // Assert
        verify(commentRepository).findAll();
        verify(commentRepository).findAll(isA(Pageable.class));
        assertEquals(0, actualAllComment.getPageNo());
        assertEquals(0, actualAllComment.getPageSize());
        assertEquals(0L, actualAllComment.getTotalElement());
        assertEquals(1, actualAllComment.getTotalPages());
        assertTrue(actualAllComment.isLastPage());
        assertEquals(commentList, actualAllComment.getContent());
    }

    /**
     * Method under test:
     * {@link CommentServiceImpl#getAllComment(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllComment2() {
        // Arrange
        when(commentRepository.findAll()).thenThrow(
                new ResourceNotFoundException("Fetching all comments with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}",
                        "Fetching all comments with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}", 42L));
        when(commentRepository.findAll(Mockito.<Pageable>any())).thenReturn(new PageImpl<>(new ArrayList<>()));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> commentServiceImpl.getAllComment(1, 3, "Sort By", "Sort Dir"));
        verify(commentRepository).findAll();
        verify(commentRepository).findAll(isA(Pageable.class));
    }

    /**
     * Method under test:
     * {@link CommentServiceImpl#getAllComment(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllComment3() {
        // Arrange
        ArrayList<Comment> content = new ArrayList<>();
        content.add(comment);
        PageImpl<Comment> pageImpl = new PageImpl<>(content);
        when(commentRepository.findAll()).thenReturn(new ArrayList<>());
        when(commentRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);
        // Act
        CommentResponse actualAllComment = commentServiceImpl.getAllComment(1, 3, "Sort By", "Sort Dir");
        // Assert
        verify(commentRepository).findAll();
        verify(commentRepository).findAll(isA(Pageable.class));
        List<CommentDto> content2 = actualAllComment.getContent();
        assertEquals(1, content2.size());
        CommentDto getResult = content2.get(0);
        assertEquals("Not all who wander are lost", getResult.getContent());
        assertEquals(0, actualAllComment.getPageNo());
        assertEquals(1, actualAllComment.getPageSize());
        assertEquals(1, actualAllComment.getTotalPages());
        assertEquals(1, getResult.getId().intValue());
        assertEquals(1L, actualAllComment.getTotalElement());
        assertTrue(actualAllComment.isLastPage());
    }

    /**
     * Method under test:
     * {@link CommentServiceImpl#getAllComment(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllComment4() {
        // Arrange
        ArrayList<Comment> content = new ArrayList<>();
        content.add(comment2);
        content.add(comment);
        PageImpl<Comment> pageImpl = new PageImpl<>(content);
        when(commentRepository.findAll()).thenReturn(new ArrayList<>());
        when(commentRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);

        // Act
        CommentResponse actualAllComment = commentServiceImpl.getAllComment(1, 3, "Sort By", "Sort Dir");

        // Assert
        verify(commentRepository).findAll();
        verify(commentRepository).findAll(isA(Pageable.class));
        List<CommentDto> content2 = actualAllComment.getContent();
        assertEquals(2, content2.size());
        CommentDto getResult = content2.get(0);
        assertEquals("Fetching all comments with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}",
                getResult.getContent());
        CommentDto getResult2 = content2.get(1);
        assertEquals("Not all who wander are lost", getResult2.getContent());
        assertEquals(0, actualAllComment.getPageNo());
        assertEquals(1, actualAllComment.getTotalPages());
        assertEquals(1, getResult2.getId().intValue());
        assertEquals(2, actualAllComment.getPageSize());
        assertEquals(2, getResult.getId().intValue());
        assertEquals(2L, actualAllComment.getTotalElement());
        assertTrue(actualAllComment.isLastPage());
    }

    /**
     * Method under test:
     * {@link CommentServiceImpl#getAllComment(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllComment5() {
        // Arrange
        ArrayList<Comment> commentList = new ArrayList<>();
        when(commentRepository.findAll()).thenReturn(commentList);
        when(commentRepository.findAll(Mockito.<Pageable>any())).thenReturn(new PageImpl<>(new ArrayList<>()));

        // Act
        CommentResponse actualAllComment = commentServiceImpl.getAllComment(1, 3, "Sort By", "asc");

        // Assert
        verify(commentRepository).findAll();
        verify(commentRepository).findAll(isA(Pageable.class));
        assertEquals(0, actualAllComment.getPageNo());
        assertEquals(0, actualAllComment.getPageSize());
        assertEquals(0L, actualAllComment.getTotalElement());
        assertEquals(1, actualAllComment.getTotalPages());
        assertTrue(actualAllComment.isLastPage());
        assertEquals(commentList, actualAllComment.getContent());
    }

    /**
     * Method under test: {@link CommentServiceImpl#updateComment(int, CommentDto)}
     */
    @Test
    void testUpdateComment() {
        // Arrange
        when(commentRepository.save(Mockito.<Comment>any())).thenReturn(comment2);
        when(commentRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(comment));

        // Act
        CommentDto actualUpdateCommentResult = commentServiceImpl.updateComment(1, new CommentDto());

        // Assert
        verify(commentRepository).findById(eq(1));
        verify(commentRepository).save(isA(Comment.class));
        assertNull(actualUpdateCommentResult.getContent());
        assertEquals(1, actualUpdateCommentResult.getId().intValue());
    }

    /**
     * Method under test: {@link CommentServiceImpl#updateComment(int, CommentDto)}
     */
    @Test
    void testUpdateComment2() {
        // Arrange
        when(commentRepository.save(Mockito.<Comment>any()))
                .thenThrow(new ResourceNotFoundException("updating comment: {} {}", "updating comment: {} {}", 42L));
        when(commentRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(comment));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> commentServiceImpl.updateComment(1, new CommentDto()));
        verify(commentRepository).findById(eq(1));
        verify(commentRepository).save(isA(Comment.class));
    }
}
