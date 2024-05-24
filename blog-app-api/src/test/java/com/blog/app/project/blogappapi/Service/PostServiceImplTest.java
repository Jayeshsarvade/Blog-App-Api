package com.blog.app.project.blogappapi.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.blog.app.project.blogappapi.payload.PostResponse;
import com.blog.app.project.blogappapi.repository.CategoryRepository;
import com.blog.app.project.blogappapi.repository.PostRepository;
import com.blog.app.project.blogappapi.repository.UserRepository;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import com.blog.app.project.blogappapi.service.impl.PostServiceImpl;
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

@ContextConfiguration(classes = {PostServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class PostServiceImplTest {
    @MockBean
    private AddressClient addressClient;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private PostRepository postRepository;

    @Autowired
    private PostServiceImpl postServiceImpl;

    @MockBean
    private UserRepository userRepository;

    /**
     * Method under test:
     * {@link PostServiceImpl#createPost(PostDto, Integer, Integer)}
     */

    private Category category;
    private Category category2;
    private Category category3;
    private User user;
    private Post post;
    private User user2;
    private AddressDto addressDto;
    private CategoryDto categoryDto;
    private Post post2;
    private Comment comment;
    private Comment comment2;
    private UserDto userDto;
    private User user3;
    private User user4;
    private User user5;
    @BeforeEach
    public void setUp() {
        category = new Category();
        category.setCategoryDescription("Category Description");
        category.setCategoryId(1);
        category.setCategoryTitle("Dr");
        category.setPost(new ArrayList<>());

        category2 = new Category();
        category2.setCategoryDescription("Category Description");
        category2.setCategoryId(1);
        category2.setCategoryTitle("Dr");
        category2.setPost(new ArrayList<>());

        category3 = new Category();
        category3.setCategoryDescription("asc");
        category3.setCategoryId(2);
        category3.setCategoryTitle("Mr");
        category3.setPost(new ArrayList<>());

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
        post.setCategory(category2);
        post.setComments(new HashSet<>());
        post.setContent("Not all who wander are lost");
        post.setImageName("Image Name");
        post.setPostId(1);
        post.setTitle("Dr");
        post.setUser(user);

        post2 = new Post();
        post2.setAddDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        post2.setCategory(category2);
        post2.setComments(new HashSet<>());
        post2.setContent("Not all who wander are lost");
        post2.setImageName("Image Name");
        post2.setPostId(1);
        post2.setTitle("Dr");
        post2.setUser(user2);



        user2 = new User();
        user2.setAbout("About");
        user2.setComments(new HashSet<>());
        user2.setEmail("jane.doe@example.org");
        user2.setId(1);
        user2.setName("Name");
        user2.setPassword("iloveyou");
        user2.setPost(new ArrayList<>());

        user3 = new User();
        user3.setAbout("About");
        user3.setComments(new HashSet<>());
        user3.setEmail("jane.doe@example.org");
        user3.setId(1);
        user3.setName("Name");
        user3.setPassword("iloveyou");
        user3.setPost(new ArrayList<>());

        user4 = new User();
        user4.setAbout("asc");
        user4.setComments(new HashSet<>());
        user4.setEmail("john.smith@example.org");
        user4.setId(2);
        user4.setName("asc");
        user4.setPassword("Fetching all posts with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}");
        user4.setPost(new ArrayList<>());

        user5 = new User();
        user5.setAbout("Fetching all posts with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}");
        user5.setComments(new HashSet<>());
        user5.setEmail("jane.doe@example.org");
        user5.setId(1);
        user5.setName("Fetching all posts with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}");
        user5.setPassword("iloveyou");
        user5.setPost(new ArrayList<>());

        comment = new Comment();
        comment.setContent("Not all who wander are lost");
        comment.setId(1);
        comment.setPost(post);
        comment.setUser(user2);

        comment2 = new Comment();
        comment2.setContent("Fetching all posts with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}");
        comment2.setId(2);
        comment2.setPost(post2);
        comment2.setUser(user4);

        addressDto = AddressDto.builder()
                .city("Oxford")
                .id(1)
                .lane1("Lane1")
                .lane2("Lane2")
                .state("MD")
                .build();

        categoryDto = CategoryDto.builder()
                .categoryDescription("Category Description")
                .categoryId(1)
                .categoryTitle("Dr")
                .build();

        userDto = UserDto.builder()
                .about("About")
                .email("jane.doe@example.org")
                .id(1)
                .name("Name")
                .password("iloveyou")
                .build();

    }

    @Test
    void testCreatePost() {
        // Arrange
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(category));
        when(postRepository.save(Mockito.<Post>any())).thenReturn(post);
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(user2));
        // Act
        PostDto actualCreatePostResult = postServiceImpl.createPost(new PostDto(), 1, 1);
        // Assert
        verify(categoryRepository).findById(eq(1));
        verify(userRepository).findById(eq(1));
        verify(postRepository).save(isA(Post.class));
        UserDto user3 = actualCreatePostResult.getUser();
        assertEquals("About", user3.getAbout());
        CategoryDto category3 = actualCreatePostResult.getCategory();
        assertEquals("Category Description", category3.getCategoryDescription());
        assertEquals("Dr", category3.getCategoryTitle());
        assertEquals("Name", user3.getName());
        assertEquals("def.png", actualCreatePostResult.getImageName());
        assertEquals("iloveyou", user3.getPassword());
        assertEquals("jane.doe@example.org", user3.getEmail());
        assertNull(user3.getAddressDto());
        assertNull(actualCreatePostResult.getContent());
        assertNull(actualCreatePostResult.getTitle());
        assertNull(actualCreatePostResult.getComments());
        assertEquals(0, actualCreatePostResult.getPostId());
        assertEquals(1, user3.getId());
        assertEquals(1, category3.getCategoryId().intValue());
    }
    /**
     * Method under test:
     * {@link PostServiceImpl#createPost(PostDto, Integer, Integer)}
     */
    @Test
    void testCreatePost2() {
        // Arrange
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(category));
        when(postRepository.save(Mockito.<Post>any()))
                .thenThrow(new ResourceNotFoundException("Creating post: {} {} {}", "Creating post: {} {} {}", 42L));
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(user));
        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> postServiceImpl.createPost(new PostDto(), 1, 1));
        verify(categoryRepository).findById(eq(1));
        verify(userRepository).findById(eq(1));
        verify(postRepository).save(isA(Post.class));
    }

    /**
     * Method under test:
     * {@link PostServiceImpl#createPost(PostDto, Integer, Integer)}
     */
    @Test
    void testCreatePost3() {
        // Arrange
        Optional<Category> emptyResult = Optional.empty();
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(user));
        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> postServiceImpl.createPost(new PostDto(), 1, 1));
        verify(categoryRepository).findById(eq(1));
        verify(userRepository).findById(eq(1));
    }
    /**
     * Method under test:
     * {@link PostServiceImpl#createPost(PostDto, Integer, Integer)}
     */
    @Test
    void testCreatePost4() {
        // Arrange
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(category));
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);
        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> postServiceImpl.createPost(new PostDto(), 1, 1));
        verify(userRepository).findById(eq(1));
    }
    /**
     * Method under test:
     * {@link PostServiceImpl#createPost(PostDto, Integer, Integer)}
     */
    @Test
    void testCreatePost5() {
        // Arrange
        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(category));
        when(postRepository.save(Mockito.<Post>any())).thenReturn(post);
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn( Optional.of(user2));
        PostDto.PostDtoBuilder builderResult = PostDto.builder();

        PostDto.PostDtoBuilder categoryResult = builderResult.category(categoryDto);
        PostDto.PostDtoBuilder titleResult = categoryResult.comments(new HashSet<>())
                .content("Not all who wander are lost")
                .imageName("Image Name")
                .postId(1)
                .title("Dr");
        PostDto postDto = titleResult.user(userDto).build();

        // Act
        PostDto actualCreatePostResult = postServiceImpl.createPost(postDto, 1, 1);
        // Assert
        verify(addressClient).getAddressByUserId(eq(1));
        verify(categoryRepository).findById(eq(1));
        verify(userRepository).findById(eq(1));
        verify(postRepository).save(isA(Post.class));
        UserDto user4 = actualCreatePostResult.getUser();
        assertEquals("About", user4.getAbout());
        CategoryDto category4 = actualCreatePostResult.getCategory();
        assertEquals("Category Description", category4.getCategoryDescription());
        assertEquals("Dr", category4.getCategoryTitle());
        assertEquals("Dr", actualCreatePostResult.getTitle());
        assertEquals("Name", user4.getName());
        assertEquals("Not all who wander are lost", actualCreatePostResult.getContent());
        AddressDto addressDto = user4.getAddressDto();
        assertEquals("Oxford", addressDto.getCity());
        assertEquals("def.png", actualCreatePostResult.getImageName());
        assertEquals("iloveyou", user4.getPassword());
        assertEquals("jane.doe@example.org", user4.getEmail());
        assertNull(actualCreatePostResult.getComments());
        assertEquals(1, actualCreatePostResult.getPostId());
        assertEquals(1, user4.getId());
        assertEquals(1, category4.getCategoryId().intValue());
        assertSame(addressDto, postDto.getUser().getAddressDto());
    }

    /**
     * Method under test: {@link PostServiceImpl#updatePost(PostDto, Integer)}
     */
    @Test
    void testUpdatePost() {
        // Arrange
        when(postRepository.save(Mockito.<Post>any())).thenReturn(post2);
        when(postRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(post));
        // Act
        PostDto actualUpdatePostResult = postServiceImpl.updatePost(new PostDto(), 1);
        // Assert
        verify(postRepository).findById(eq(1));
        verify(postRepository).save(isA(Post.class));
        UserDto user3 = actualUpdatePostResult.getUser();
        assertEquals("About", user3.getAbout());
        CategoryDto category3 = actualUpdatePostResult.getCategory();
        assertEquals("Category Description", category3.getCategoryDescription());
        assertEquals("Dr", category3.getCategoryTitle());
        assertEquals("Name", user3.getName());
        assertEquals("iloveyou", user3.getPassword());
        assertEquals("jane.doe@example.org", user3.getEmail());
        assertNull(user3.getAddressDto());
        assertNull(actualUpdatePostResult.getContent());
        assertNull(actualUpdatePostResult.getImageName());
        assertNull(actualUpdatePostResult.getTitle());
        assertEquals(1, actualUpdatePostResult.getPostId());
        assertEquals(1, user3.getId());
        assertEquals(1, category3.getCategoryId().intValue());
        assertTrue(actualUpdatePostResult.getComments().isEmpty());
        assertSame(post.getAddDate(), actualUpdatePostResult.getAddedDate());
    }

    /**
     * Method under test: {@link PostServiceImpl#updatePost(PostDto, Integer)}
     */
    @Test
    void testUpdatePost2() {
        // Arrange
        when(postRepository.save(Mockito.<Post>any()))
                .thenThrow(new ResourceNotFoundException("updating post: {} {}", "updating post: {} {}", 42L));
        when(postRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(post));
        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> postServiceImpl.updatePost(new PostDto(), 1));
        verify(postRepository).findById(eq(1));
        verify(postRepository).save(isA(Post.class));
    }

    /**
     * Method under test: {@link PostServiceImpl#updatePost(PostDto, Integer)}
     */
    @Test
    void testUpdatePost3() {
        // Arrange
        HashSet<Comment> comments = new HashSet<>();
        comments.add(comment);

        Post post2 = new Post();
        Date addDate = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        post2.setAddDate(addDate);
        post2.setCategory(category);
        post2.setComments(comments);
        post2.setContent("Not all who wander are lost");
        post2.setImageName("Image Name");
        post2.setPostId(1);
        post2.setTitle("Dr");
        post2.setUser(user3);
        Optional<Post> ofResult = Optional.of(post2);

        Category category3 = new Category();
        category3.setCategoryDescription("Category Description");
        category3.setCategoryId(1);
        category3.setCategoryTitle("Dr");
        category3.setPost(new ArrayList<>());

        User user4 = new User();
        user4.setAbout("About");
        user4.setComments(new HashSet<>());
        user4.setEmail("jane.doe@example.org");
        user4.setId(1);
        user4.setName("Name");
        user4.setPassword("iloveyou");
        user4.setPost(new ArrayList<>());

        Post post3 = new Post();
        post3.setAddDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        post3.setCategory(category3);
        post3.setComments(new HashSet<>());
        post3.setContent("Not all who wander are lost");
        post3.setImageName("Image Name");
        post3.setPostId(1);
        post3.setTitle("Dr");
        post3.setUser(user4);
        when(postRepository.save(Mockito.<Post>any())).thenReturn(post3);
        when(postRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(post2));

        // Act
        PostDto actualUpdatePostResult = postServiceImpl.updatePost(new PostDto(), 1);

        // Assert
        verify(postRepository).findById(eq(1));
        verify(postRepository).save(isA(Post.class));
        UserDto user5 = actualUpdatePostResult.getUser();
        assertEquals("About", user5.getAbout());
        CategoryDto category4 = actualUpdatePostResult.getCategory();
        assertEquals("Category Description", category4.getCategoryDescription());
        assertEquals("Dr", category4.getCategoryTitle());
        assertEquals("Name", user5.getName());
        assertEquals("iloveyou", user5.getPassword());
        assertEquals("jane.doe@example.org", user5.getEmail());
        assertNull(user5.getAddressDto());
        assertNull(actualUpdatePostResult.getContent());
        assertNull(actualUpdatePostResult.getImageName());
        assertNull(actualUpdatePostResult.getTitle());
        assertEquals(1, actualUpdatePostResult.getPostId());
        assertEquals(1, user5.getId());
        assertEquals(1, category4.getCategoryId().intValue());
        assertEquals(1, actualUpdatePostResult.getComments().size());
        assertSame(addDate, actualUpdatePostResult.getAddedDate());
    }

    /**
     * Method under test: {@link PostServiceImpl#deletePost(Integer)}
     */
    @Test
    void testDeletePost() {
        // Arrange
        doNothing().when(postRepository).delete(Mockito.<Post>any());
        when(postRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(post));
        // Act
        postServiceImpl.deletePost(1);
        // Assert that nothing has changed
        verify(postRepository).delete(isA(Post.class));
        verify(postRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link PostServiceImpl#deletePost(Integer)}
     */
    @Test
    void testDeletePost2() {
        // Arrange
        doThrow(new ResourceNotFoundException("deleting post: {}", "deleting post: {}", 42L)).when(postRepository)
                .delete(Mockito.<Post>any());
        when(postRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(post));
        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> postServiceImpl.deletePost(1));
        verify(postRepository).delete(isA(Post.class));
        verify(postRepository).findById(eq(1));
    }
    /**
     * Method under test: {@link PostServiceImpl#deletePost(Integer)}
     */
    @Test
    void testDeletePost3() {
        // Arrange
        Optional<Post> emptyResult = Optional.empty();
        when(postRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);
        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> postServiceImpl.deletePost(1));
        verify(postRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link PostServiceImpl#getPostById(Integer)}
     */
    @Test
    void testGetPostById() {
        // Arrange
        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);
        when(postRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(post));
        // Act
        PostDto actualPostById = postServiceImpl.getPostById(1);

        // Assert
        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findById(eq(1));
        UserDto user2 = actualPostById.getUser();
        assertEquals("About", user2.getAbout());
        CategoryDto category2 = actualPostById.getCategory();
        assertEquals("Category Description", category2.getCategoryDescription());
        assertEquals("Dr", category2.getCategoryTitle());
        assertEquals("Dr", actualPostById.getTitle());
        assertEquals("Image Name", actualPostById.getImageName());
        assertEquals("Name", user2.getName());
        assertEquals("Not all who wander are lost", actualPostById.getContent());
        assertEquals("Oxford", user2.getAddressDto().getCity());
        assertEquals("iloveyou", user2.getPassword());
        assertEquals("jane.doe@example.org", user2.getEmail());
        assertEquals(1, actualPostById.getPostId());
        assertEquals(1, user2.getId());
        assertEquals(1, category2.getCategoryId().intValue());
        assertTrue(actualPostById.getComments().isEmpty());
        assertSame(post.getAddDate(), actualPostById.getAddedDate());
    }

    /**
     * Method under test: {@link PostServiceImpl#getPostById(Integer)}
     */
    @Test
    void testGetPostById2() {
        // Arrange
        when(addressClient.getAddressByUserId(anyInt()))
                .thenThrow(new ResourceNotFoundException("get post by Id: {}", "get post by Id: {}", 42L));
        when(postRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(post));
        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> postServiceImpl.getPostById(1));
        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link PostServiceImpl#getPostById(Integer)}
     */
    @Test
    void testGetPostById3() {
        // Arrange
        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        HashSet<Comment> comments = new HashSet<>();
        comments.add(comment);

        Post post2 = new Post();
        Date addDate = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        post2.setAddDate(addDate);
        post2.setCategory(category);
        post2.setComments(comments);
        post2.setContent("Not all who wander are lost");
        post2.setImageName("Image Name");
        post2.setPostId(1);
        post2.setTitle("Dr");
        post2.setUser(user3);
        Optional<Post> ofResult = Optional.of(post2);
        when(postRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        // Act
        PostDto actualPostById = postServiceImpl.getPostById(1);
        // Assert
        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findById(eq(1));
        UserDto user4 = actualPostById.getUser();
        assertEquals("About", user4.getAbout());
        CategoryDto category3 = actualPostById.getCategory();
        assertEquals("Category Description", category3.getCategoryDescription());
        assertEquals("Dr", category3.getCategoryTitle());
        assertEquals("Dr", actualPostById.getTitle());
        assertEquals("Image Name", actualPostById.getImageName());
        assertEquals("Name", user4.getName());
        assertEquals("Not all who wander are lost", actualPostById.getContent());
        assertEquals("Oxford", user4.getAddressDto().getCity());
        assertEquals("iloveyou", user4.getPassword());
        assertEquals("jane.doe@example.org", user4.getEmail());
        assertEquals(1, actualPostById.getPostId());
        assertEquals(1, user4.getId());
        assertEquals(1, category3.getCategoryId().intValue());
        assertEquals(1, actualPostById.getComments().size());
        assertSame(addDate, actualPostById.getAddedDate());
    }

    /**
     * Method under test: {@link PostServiceImpl#getPostById(Integer)}
     */
    @Test
    void testGetPostById4() {
        // Arrange
        Optional<Post> emptyResult = Optional.empty();
        when(postRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> postServiceImpl.getPostById(1));
        verify(postRepository).findById(eq(1));
    }

    /**
     * Method under test:
     * {@link PostServiceImpl#getAllPost(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllPost() {
        // Arrange
        ArrayList<Post> content = new ArrayList<>();
        when(postRepository.findAll(Mockito.<Pageable>any())).thenReturn(new PageImpl<>(content));
        // Act
        PostResponse actualAllPost = postServiceImpl.getAllPost(1, 3, "Sort By", "Sort Dir");
        // Assert
        verify(postRepository).findAll(isA(Pageable.class));
        assertEquals(0, actualAllPost.getPageNo());
        assertEquals(0, actualAllPost.getPageSize());
        assertEquals(0L, actualAllPost.getTotalElement());
        assertEquals(1, actualAllPost.getTotalPages());
        assertTrue(actualAllPost.isLastPage());
        assertEquals(content, actualAllPost.getContent());
    }

    /**
     * Method under test:
     * {@link PostServiceImpl#getAllPost(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllPost2() {
        // Arrange

        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        ArrayList<Post> content = new ArrayList<>();
        content.add(post);
        PageImpl<Post> pageImpl = new PageImpl<>(content);
        when(postRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);
        // Act
        PostResponse actualAllPost = postServiceImpl.getAllPost(1, 3, "Sort By", "Sort Dir");
        // Assert
        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findAll(isA(Pageable.class));
        assertEquals(0, actualAllPost.getPageNo());
        assertEquals(1, actualAllPost.getPageSize());
        assertEquals(1, actualAllPost.getTotalPages());
        assertEquals(1, actualAllPost.getContent().size());
        assertEquals(1L, actualAllPost.getTotalElement());
        assertTrue(actualAllPost.isLastPage());
    }

    /**
     * Method under test:
     * {@link PostServiceImpl#getAllPost(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllPost3() {
        // Arrange
        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        Post post2 = new Post();
        post2.setAddDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        post2.setCategory(category2);
        post2.setComments(new HashSet<>());
        post2.setContent("Fetching all posts with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}");
        post2.setImageName("asc");
        post2.setPostId(2);
        post2.setTitle("Mr");
        post2.setUser(user2);

        ArrayList<Post> content = new ArrayList<>();
        content.add(post2);
        content.add(post);
        PageImpl<Post> pageImpl = new PageImpl<>(content);
        when(postRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);

        // Act
        PostResponse actualAllPost = postServiceImpl.getAllPost(1, 3, "Sort By", "Sort Dir");

        // Assert
        verify(addressClient, atLeast(1)).getAddressByUserId(anyInt());
        verify(postRepository).findAll(isA(Pageable.class));
        assertEquals(0, actualAllPost.getPageNo());
        assertEquals(1, actualAllPost.getTotalPages());
        assertEquals(2, actualAllPost.getPageSize());
        assertEquals(2, actualAllPost.getContent().size());
        assertEquals(2L, actualAllPost.getTotalElement());
        assertTrue(actualAllPost.isLastPage());
    }

    /**
     * Method under test:
     * {@link PostServiceImpl#getAllPost(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllPost4() {
        // Arrange
        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);
        HashSet<Comment> comments = new HashSet<>();
        comments.add(comment);

        Post post2 = new Post();
        post2.setAddDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        post2.setCategory(category);
        post2.setComments(comments);
        post2.setContent("Not all who wander are lost");
        post2.setImageName("Fetching all posts with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}");
        post2.setPostId(1);
        post2.setTitle("Dr");
        post2.setUser(user3);

        ArrayList<Post> content = new ArrayList<>();
        content.add(post2);
        PageImpl<Post> pageImpl = new PageImpl<>(content);
        when(postRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);
        // Act
        PostResponse actualAllPost = postServiceImpl.getAllPost(1, 3, "Sort By", "Sort Dir");
        // Assert
        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findAll(isA(Pageable.class));
        assertEquals(0, actualAllPost.getPageNo());
        assertEquals(1, actualAllPost.getPageSize());
        assertEquals(1, actualAllPost.getTotalPages());
        assertEquals(1, actualAllPost.getContent().size());
        assertEquals(1L, actualAllPost.getTotalElement());
        assertTrue(actualAllPost.isLastPage());
    }
    /**
     * Method under test:
     * {@link PostServiceImpl#getAllPost(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllPost5() {
        // Arrange

        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        HashSet<Comment> comments = new HashSet<>();
        comments.add(comment2);
        comments.add(comment);

        Post post3 = new Post();
        post3.setAddDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        post3.setCategory(category);
        post3.setComments(comments);
        post3.setContent("Not all who wander are lost");
        post3.setImageName("Fetching all posts with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}");
        post3.setPostId(1);
        post3.setTitle("Dr");
        post3.setUser(user5);

        ArrayList<Post> content = new ArrayList<>();
        content.add(post3);
        PageImpl<Post> pageImpl = new PageImpl<>(content);
        when(postRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);
        // Act
        PostResponse actualAllPost = postServiceImpl.getAllPost(1, 3, "Sort By", "Sort Dir");
        // Assert
        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findAll(isA(Pageable.class));
        assertEquals(0, actualAllPost.getPageNo());
        assertEquals(1, actualAllPost.getPageSize());
        assertEquals(1, actualAllPost.getTotalPages());
        assertEquals(1, actualAllPost.getContent().size());
        assertEquals(1L, actualAllPost.getTotalElement());
        assertTrue(actualAllPost.isLastPage());
    }

    /**
     * Method under test:
     * {@link PostServiceImpl#getAllPost(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllPost6() {
        // Arrange

        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        ArrayList<Post> content = new ArrayList<>();
        content.add(post);
        PageImpl<Post> pageImpl = new PageImpl<>(content);
        when(postRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);

        // Act
        PostResponse actualAllPost = postServiceImpl.getAllPost(1, 3, "Sort By", "asc");

        // Assert
        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findAll(isA(Pageable.class));
        assertEquals(0, actualAllPost.getPageNo());
        assertEquals(1, actualAllPost.getPageSize());
        assertEquals(1, actualAllPost.getTotalPages());
        assertEquals(1, actualAllPost.getContent().size());
        assertEquals(1L, actualAllPost.getTotalElement());
        assertTrue(actualAllPost.isLastPage());
    }

    /**
     * Method under test: {@link PostServiceImpl#getPostsByCategory(Integer)}
     */
    @Test
    void testGetPostsByCategory() {
        // Arrange
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(category));
        when(postRepository.findByCategory(Mockito.<Category>any())).thenReturn(new ArrayList<>());

        // Act
        List<PostDto> actualPostsByCategory = postServiceImpl.getPostsByCategory(1);
        // Assert
        verify(postRepository).findByCategory(isA(Category.class));
        verify(categoryRepository).findById(eq(1));
        assertTrue(actualPostsByCategory.isEmpty());
    }

    /**
     * Method under test: {@link PostServiceImpl#getPostsByCategory(Integer)}
     */
    @Test
    void testGetPostsByCategory2() {
        // Arrange

        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(category));
        when(postRepository.findByCategory(Mockito.<Category>any()))
                .thenThrow(new ResourceNotFoundException("getting post by category: {}", "getting post by category: {}", 42L));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> postServiceImpl.getPostsByCategory(1));
        verify(postRepository).findByCategory(isA(Category.class));
        verify(categoryRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link PostServiceImpl#getPostsByCategory(Integer)}
     */
    @Test
    void testGetPostsByCategory3() {
        // Arrange
        Optional<Category> emptyResult = Optional.empty();
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> postServiceImpl.getPostsByCategory(1));
        verify(categoryRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link PostServiceImpl#getPostsByCategory(Integer)}
     */
    @Test
    void testGetPostsByCategory4() {
        // Arrange
        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(category));

        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post);
        when(postRepository.findByCategory(Mockito.<Category>any())).thenReturn(postList);
        // Act
        List<PostDto> actualPostsByCategory = postServiceImpl.getPostsByCategory(1);
        // Assert
        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findByCategory(isA(Category.class));
        verify(categoryRepository).findById(eq(1));
        assertEquals(1, actualPostsByCategory.size());
    }

    /**
     * Method under test: {@link PostServiceImpl#getPostsByCategory(Integer)}
     */
    @Test
    void testGetPostsByCategory5() {
        // Arrange

        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(category));

        Post post2 = new Post();
        post2.setAddDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        post2.setCategory(category3);
        post2.setComments(new HashSet<>());
        post2.setContent("getting post by category: {}");
        post2.setImageName("got post by category: {}");
        post2.setPostId(2);
        post2.setTitle("Mr");
        post2.setUser(user2);

        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post2);
        postList.add(post);
        when(postRepository.findByCategory(Mockito.<Category>any())).thenReturn(postList);
        // Act
        List<PostDto> actualPostsByCategory = postServiceImpl.getPostsByCategory(1);
        // Assert
        verify(addressClient, atLeast(1)).getAddressByUserId(anyInt());
        verify(postRepository).findByCategory(isA(Category.class));
        verify(categoryRepository).findById(eq(1));
        assertEquals(2, actualPostsByCategory.size());
    }

    /**
     * Method under test: {@link PostServiceImpl#getPostsByCategory(Integer)}
     */
    @Test
    void testGetPostsByCategory6() {
        // Arrange
        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(category));

        HashSet<Comment> comments = new HashSet<>();
        comments.add(comment);

        Post post2 = new Post();
        post2.setAddDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        post2.setCategory(category2);
        post2.setComments(comments);
        post2.setContent("Not all who wander are lost");
        post2.setImageName("getting post by category: {}");
        post2.setPostId(1);
        post2.setTitle("Dr");
        post2.setUser(user3);

        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post2);
        when(postRepository.findByCategory(Mockito.<Category>any())).thenReturn(postList);
        // Act
        List<PostDto> actualPostsByCategory = postServiceImpl.getPostsByCategory(1);
        // Assert
        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findByCategory(isA(Category.class));
        verify(categoryRepository).findById(eq(1));
        assertEquals(1, actualPostsByCategory.size());
    }
    /**
     * Method under test: {@link PostServiceImpl#getPostsByCategory(Integer)}
     */
    @Test
    void testGetPostsByCategory7() {
        // Arrange
        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(category));

        HashSet<Comment> comments = new HashSet<>();
        comments.add(comment2);
        comments.add(comment);

        Post post3 = new Post();
        post3.setAddDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        post3.setCategory(category2);
        post3.setComments(comments);
        post3.setContent("Not all who wander are lost");
        post3.setImageName("getting post by category: {}");
        post3.setPostId(1);
        post3.setTitle("Dr");
        post3.setUser(user3);

        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post3);
        when(postRepository.findByCategory(Mockito.<Category>any())).thenReturn(postList);
        // Act
        List<PostDto> actualPostsByCategory = postServiceImpl.getPostsByCategory(1);
        // Assert
        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findByCategory(isA(Category.class));
        verify(categoryRepository).findById(eq(1));
        assertEquals(1, actualPostsByCategory.size());
    }

    /**
     * Method under test: {@link PostServiceImpl#getPostsByUser(Integer)}
     */
    @Test
    void testGetPostsByUser() {
        // Arrange
        when(postRepository.findByUser(Mockito.<User>any())).thenReturn(new ArrayList<>());
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(user));

        // Act
        List<PostDto> actualPostsByUser = postServiceImpl.getPostsByUser(1);

        // Assert
        verify(postRepository).findByUser(isA(User.class));
        verify(userRepository).findById(eq(1));
        assertTrue(actualPostsByUser.isEmpty());
    }

    /**
     * Method under test: {@link PostServiceImpl#getPostsByUser(Integer)}
     */
    @Test
    void testGetPostsByUser2() {
        // Arrange
        when(postRepository.findByUser(Mockito.<User>any()))
                .thenThrow(new ResourceNotFoundException("getting post by user: {}", "getting post by user: {}", 42L));
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(user));
        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> postServiceImpl.getPostsByUser(1));
        verify(postRepository).findByUser(isA(User.class));
        verify(userRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link PostServiceImpl#getPostsByUser(Integer)}
     */
    @Test
    void testGetPostsByUser3() {
        // Arrange
        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post);
        when(postRepository.findByUser(Mockito.<User>any())).thenReturn(postList);
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(user2));

        // Act
        List<PostDto> actualPostsByUser = postServiceImpl.getPostsByUser(1);

        // Assert
        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findByUser(isA(User.class));
        verify(userRepository).findById(eq(1));
        assertEquals(1, actualPostsByUser.size());
    }

    /**
     * Method under test: {@link PostServiceImpl#getPostsByUser(Integer)}
     */
    @Test
    void testGetPostsByUser4() {
        // Arrange
        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        Post post2 = new Post();
        post2.setAddDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        post2.setCategory(category2);
        post2.setComments(new HashSet<>());
        post2.setContent("getting post by user: {}");
        post2.setImageName("got post by user: {}");
        post2.setPostId(2);
        post2.setTitle("Mr");
        post2.setUser(user2);

        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post2);
        postList.add(post);
        when(postRepository.findByUser(Mockito.<User>any())).thenReturn(postList);
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(user3));
        // Act
        List<PostDto> actualPostsByUser = postServiceImpl.getPostsByUser(1);
        // Assert
        verify(addressClient, atLeast(1)).getAddressByUserId(eq(1));
        verify(postRepository).findByUser(isA(User.class));
        verify(userRepository).findById(eq(1));
        assertEquals(2, actualPostsByUser.size());
    }

    /**
     * Method under test: {@link PostServiceImpl#getPostsByUser(Integer)}
     */
    @Test
    void testGetPostsByUser5() {
        // Arrange
        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        HashSet<Comment> comments = new HashSet<>();
        comments.add(comment);

        Post post2 = new Post();
        post2.setAddDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        post2.setCategory(category);
        post2.setComments(comments);
        post2.setContent("Not all who wander are lost");
        post2.setImageName("getting post by user: {}");
        post2.setPostId(1);
        post2.setTitle("Dr");
        post2.setUser(user3);

        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post2);
        when(postRepository.findByUser(Mockito.<User>any())).thenReturn(postList);
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(user4));
        // Act
        List<PostDto> actualPostsByUser = postServiceImpl.getPostsByUser(1);
        // Assert
        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findByUser(isA(User.class));
        verify(userRepository).findById(eq(1));
        assertEquals(1, actualPostsByUser.size());
    }

    /**
     * Method under test: {@link PostServiceImpl#getPostsByUser(Integer)}
     */
    @Test
    void testGetPostsByUser6() {
        // Arrange

        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        HashSet<Comment> comments = new HashSet<>();
        comments.add(comment2);
        comments.add(comment);

        Post post3 = new Post();
        post3.setAddDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        post3.setCategory(category);
        post3.setComments(comments);
        post3.setContent("Not all who wander are lost");
        post3.setImageName("getting post by user: {}");
        post3.setPostId(1);
        post3.setTitle("Dr");
        post3.setUser(user3);

        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post3);
        when(postRepository.findByUser(Mockito.<User>any())).thenReturn(postList);
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(user2));
        // Act
        List<PostDto> actualPostsByUser = postServiceImpl.getPostsByUser(1);
        // Assert
        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findByUser(isA(User.class));
        verify(userRepository).findById(eq(1));
        assertEquals(1, actualPostsByUser.size());
    }

    /**
     * Method under test: {@link PostServiceImpl#getPostsByUser(Integer)}
     */
    @Test
    void testGetPostsByUser7() {
        // Arrange
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);
        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> postServiceImpl.getPostsByUser(1));
        verify(userRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link PostServiceImpl#searchPosts(String)}
     */
    @Test
    void testSearchPosts() {
        // Arrange
        when(postRepository.findByTitleContaining(Mockito.<String>any())).thenReturn(new ArrayList<>());
        // Act
        List<PostDto> actualSearchPostsResult = postServiceImpl.searchPosts("Keyword");
        // Assert
        verify(postRepository).findByTitleContaining(eq("Keyword"));
        assertTrue(actualSearchPostsResult.isEmpty());
    }

    /**
     * Method under test: {@link PostServiceImpl#searchPosts(String)}
     */
    @Test
    void testSearchPosts2() {
        // Arrange
        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post);
        when(postRepository.findByTitleContaining(Mockito.<String>any())).thenReturn(postList);
        // Act
        List<PostDto> actualSearchPostsResult = postServiceImpl.searchPosts("Keyword");
        // Assert
        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findByTitleContaining(eq("Keyword"));
        assertEquals(1, actualSearchPostsResult.size());
    }

    /**
     * Method under test: {@link PostServiceImpl#searchPosts(String)}
     */
    @Test
    void testSearchPosts3() {
        // Arrange
        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        Post post2 = new Post();
        post2.setAddDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        post2.setCategory(category2);
        post2.setComments(new HashSet<>());
        post2.setContent("searching posts: {}");
        post2.setImageName("post searchred: {}");
        post2.setPostId(2);
        post2.setTitle("Mr");
        post2.setUser(user2);

        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post2);
        postList.add(post);
        when(postRepository.findByTitleContaining(Mockito.<String>any())).thenReturn(postList);
        // Act
        List<PostDto> actualSearchPostsResult = postServiceImpl.searchPosts("Keyword");
        // Assert
        verify(addressClient, atLeast(1)).getAddressByUserId(anyInt());
        verify(postRepository).findByTitleContaining(eq("Keyword"));
        assertEquals(2, actualSearchPostsResult.size());
    }

    /**
     * Method under test: {@link PostServiceImpl#searchPosts(String)}
     */
    @Test
    void testSearchPosts4() {
        // Arrange
        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        HashSet<Comment> comments = new HashSet<>();
        comments.add(comment);

        Post post2 = new Post();
        post2.setAddDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        post2.setCategory(category);
        post2.setComments(comments);
        post2.setContent("Not all who wander are lost");
        post2.setImageName("searching posts: {}");
        post2.setPostId(1);
        post2.setTitle("Dr");
        post2.setUser(user3);

        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post2);
        when(postRepository.findByTitleContaining(Mockito.<String>any())).thenReturn(postList);
        // Act
        List<PostDto> actualSearchPostsResult = postServiceImpl.searchPosts("Keyword");
        // Assert
        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findByTitleContaining(eq("Keyword"));
        assertEquals(1, actualSearchPostsResult.size());
    }

    /**
     * Method under test: {@link PostServiceImpl#searchPosts(String)}
     */
    @Test
    void testSearchPosts5() {
        // Arrange

        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        HashSet<Comment> comments = new HashSet<>();
        comments.add(comment2);
        comments.add(comment);

        Post post3 = new Post();
        post3.setAddDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        post3.setCategory(category);
        post3.setComments(comments);
        post3.setContent("Not all who wander are lost");
        post3.setImageName("searching posts: {}");
        post3.setPostId(1);
        post3.setTitle("Dr");
        post3.setUser(user5);

        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post3);
        when(postRepository.findByTitleContaining(Mockito.<String>any())).thenReturn(postList);
        // Act
        List<PostDto> actualSearchPostsResult = postServiceImpl.searchPosts("Keyword");
        // Assert
        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findByTitleContaining(eq("Keyword"));
        assertEquals(1, actualSearchPostsResult.size());
    }

    /**
     * Method under test: {@link PostServiceImpl#getUsersWhoCommented(int)}
     */
    @Test
    void testGetUsersWhoCommented() {
        // Arrange
        when(postRepository.findUsersByCommentsPostId(anyInt())).thenReturn(new ArrayList<>());
        // Act
        List<UserDto> actualUsersWhoCommented = postServiceImpl.getUsersWhoCommented(1);
        // Assert
        verify(postRepository).findUsersByCommentsPostId(eq(1));
        assertTrue(actualUsersWhoCommented.isEmpty());
    }

    /**
     * Method under test: {@link PostServiceImpl#getUsersWhoCommented(int)}
     */
    @Test
    void testGetUsersWhoCommented2() {
        // Arrange
        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        when(postRepository.findUsersByCommentsPostId(anyInt())).thenReturn(userList);
        // Act
        List<UserDto> actualUsersWhoCommented = postServiceImpl.getUsersWhoCommented(1);
        // Assert
        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findUsersByCommentsPostId(eq(1));
        assertEquals(1, actualUsersWhoCommented.size());
    }

    /**
     * Method under test: {@link PostServiceImpl#getUsersWhoCommented(int)}
     */
    @Test
    void testGetUsersWhoCommented3() {
        // Arrange
        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user2);
        userList.add(user);
        when(postRepository.findUsersByCommentsPostId(anyInt())).thenReturn(userList);
        // Act
        List<UserDto> actualUsersWhoCommented = postServiceImpl.getUsersWhoCommented(1);
        // Assert
        verify(addressClient, atLeast(1)).getAddressByUserId(anyInt());
        verify(postRepository).findUsersByCommentsPostId(eq(1));
        assertEquals(2, actualUsersWhoCommented.size());
    }

    /**
     * Method under test: {@link PostServiceImpl#getUsersWhoCommented(int)}
     */
    @Test
    void testGetUsersWhoCommented4() {
        // Arrange
        when(addressClient.getAddressByUserId(anyInt()))
                .thenThrow(new ResourceNotFoundException("Resource Name", "Field Name", 42L));

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        when(postRepository.findUsersByCommentsPostId(anyInt())).thenReturn(userList);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> postServiceImpl.getUsersWhoCommented(1));
        verify(addressClient).getAddressByUserId(eq(1));
        verify(postRepository).findUsersByCommentsPostId(eq(1));
    }
}
