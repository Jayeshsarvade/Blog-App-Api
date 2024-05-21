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
import com.blog.app.project.blogappapi.entity.Category;
import com.blog.app.project.blogappapi.entity.Comment;
import com.blog.app.project.blogappapi.entity.Post;
import com.blog.app.project.blogappapi.entity.User;
import com.blog.app.project.blogappapi.exception.ResourceNotFoundException;
import com.blog.app.project.blogappapi.payload.UserResponse;
import com.blog.app.project.blogappapi.repository.CategoryRepository;
import com.blog.app.project.blogappapi.service.impl.CategoryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    private CategoryServiceImpl categoryServiceImpl;

    @Mock 
    private CategoryRepository categoryRepository;

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
    private Category category1;

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


         category1= Category.builder()
                .categoryId(1)
                .categoryTitle("Category Title")
                .categoryDescription("Category Description")
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
                .category(category1)
                .user(user1).comments(Collections.emptySet())
                .build();

        post2 = Post.builder().postId(2)
                .title("post2").content("this is post 2")
                .imageName("post2.png").addDate(new Date())
                .category(category1)
                .user(user2).comments(Collections.emptySet())
                .build();

        post3 = Post.builder().postId(3)
                .title("post3").content("this is post 3")
                .imageName("post3.png").addDate(new Date())
                .category(category1)
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
    public void testCreateCategory_Success() {
 
        Category category = new Category();
        category.setCategoryId(categoryDto1.getCategoryId());
        category.setCategoryTitle(categoryDto1.getCategoryTitle());
        category.setCategoryDescription(categoryDto1.getCategoryDescription());

        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryDto createdCategoryDto = categoryServiceImpl.createCategory(categoryDto1);
        
        Mockito.verify(categoryRepository, Mockito.times(1)).save(any(Category.class));
        
        assertEquals(categoryDto1.getCategoryId(), createdCategoryDto.getCategoryId());
        assertEquals(categoryDto1.getCategoryTitle(), createdCategoryDto.getCategoryTitle());
        assertEquals(categoryDto1.getCategoryDescription(), createdCategoryDto.getCategoryDescription());
    }

    @Test
    public void testCreateCategory_SaveFailed() {
    	
        when(categoryRepository.save(category1)).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> categoryServiceImpl.createCategory(categoryDto1));
    }

    
    @Test
	public void testDeleteCategory_success() throws Exception {
		int categoryId = 1;

		Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category1));
		categoryServiceImpl.deleteCategory(categoryId);
		Mockito.verify(categoryRepository, Mockito.times(1)).findById(categoryId);
	}

	@Test
	public void testDeleteCategory_NotFound() throws Exception {
		int categoryId = 10;
		Mockito.when(categoryRepository.findById(categoryId))
				.thenThrow(new ResourceNotFoundException("Category", "CategoryId", categoryId));

		assertThrows(ResourceNotFoundException.class, () -> categoryServiceImpl.deleteCategory(categoryId));
		Mockito.verify(categoryRepository, Mockito.times(1)).findById(categoryId);
	}
	
	@Test
	public void testGetCategory_succes()throws Exception{
		
        Integer categoryId = 1;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category1));

        CategoryDto categoryDto = categoryServiceImpl.getCategory(categoryId);

        Mockito.verify(categoryRepository, Mockito.times(1)).findById(categoryId);
        
        assertEquals(category1.getCategoryId(), categoryDto.getCategoryId());
        assertEquals(category1.getCategoryTitle(), categoryDto.getCategoryTitle());
        assertEquals(category1.getCategoryDescription(), categoryDto.getCategoryDescription());
    }
	
	@Test
	public void testGetCategoryById_NotFound() throws Exception {

		int categoryId = 1;
		Mockito.when(categoryRepository.findById(categoryId)).thenThrow(new ResourceNotFoundException("Category", "CategoryId", categoryId));
		assertThrows(ResourceNotFoundException.class, () -> categoryServiceImpl.getCategory(categoryId));
		Mockito.verify(categoryRepository, Mockito.times(1)).findById(categoryId);
	}
	
	@Test
    public void testUpdateCategory_Success() {
        Integer categoryId = 1;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category1));
        when(categoryRepository.save(any(Category.class))).thenReturn(category1);

        CategoryDto updatedCategoryDto = categoryServiceImpl.updateCategory(categoryDto1, categoryId);
        
        Mockito.verify(categoryRepository, Mockito.times(1)).findById(categoryId);
        Mockito.verify(categoryRepository, Mockito.times(1)).save(any(Category.class));

        assertEquals(categoryDto1.getCategoryId(), updatedCategoryDto.getCategoryId());
        assertEquals(categoryDto1.getCategoryTitle(), updatedCategoryDto.getCategoryTitle());
        assertEquals(categoryDto1.getCategoryDescription(), updatedCategoryDto.getCategoryDescription());
    }
	
	@Test
    public void testUpdateCategory_CategoryNotFound() {
        Integer categoryId = 1;

        CategoryDto updatedCategoryDto = new CategoryDto();
        updatedCategoryDto.setCategoryTitle("Updated Title");
        updatedCategoryDto.setCategoryDescription("Updated Description");

        when(categoryRepository.findById(categoryId)).thenThrow(new ResourceNotFoundException("Category","categoryId",categoryId));
        assertThrows(ResourceNotFoundException.class, () -> categoryServiceImpl.updateCategory(updatedCategoryDto, categoryId));
        Mockito.verify(categoryRepository, Mockito.times(1)).findById(categoryId);

    }
}
