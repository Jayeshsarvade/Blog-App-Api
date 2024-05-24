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

import com.blog.app.project.blogappapi.dto.CategoryDto;
import com.blog.app.project.blogappapi.entity.Category;
import com.blog.app.project.blogappapi.exception.ResourceNotFoundException;
import com.blog.app.project.blogappapi.payload.CategoryResponse;
import com.blog.app.project.blogappapi.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.blog.app.project.blogappapi.service.impl.CategoryServiceImpl;
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

@ContextConfiguration(classes = {CategoryServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class CategoryServiceImplTest {
    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryServiceImpl categoryServiceImpl;

    /**
     * Method under test: {@link CategoryServiceImpl#createCategory(CategoryDto)}
     */

    private Category category;
    private Category category2;
    @BeforeEach
    public void setUp(){
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
    }
    @Test
    void testCreateCategory() {
        // Arrange
        when(categoryRepository.save(Mockito.<Category>any())).thenReturn(category);
        // Act
        CategoryDto actualCreateCategoryResult = categoryServiceImpl.createCategory(new CategoryDto());
        // Assert
        verify(categoryRepository).save(isA(Category.class));
        assertNull(actualCreateCategoryResult.getCategoryId());
        assertNull(actualCreateCategoryResult.getCategoryDescription());
        assertNull(actualCreateCategoryResult.getCategoryTitle());
    }

    /**
     * Method under test: {@link CategoryServiceImpl#createCategory(CategoryDto)}
     */
    @Test
    void testCreateCategory2() {
        // Arrange
        when(categoryRepository.save(Mockito.<Category>any()))
                .thenThrow(new ResourceNotFoundException("Creating category: {}", "Creating category: {}", 42L));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> categoryServiceImpl.createCategory(new CategoryDto()));
        verify(categoryRepository).save(isA(Category.class));
    }

    /**
     * Method under test:
     * {@link CategoryServiceImpl#updateCategory(CategoryDto, Integer)}
     */
    @Test
    void testUpdateCategory() {
        // Arrange
        when(categoryRepository.save(Mockito.<Category>any())).thenReturn(category2);
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(category));
        // Act
        CategoryDto actualUpdateCategoryResult = categoryServiceImpl.updateCategory(new CategoryDto(), 1);
        // Assert
        verify(categoryRepository).findById(eq(1));
        verify(categoryRepository).save(isA(Category.class));
        assertNull(actualUpdateCategoryResult.getCategoryDescription());
        assertNull(actualUpdateCategoryResult.getCategoryTitle());
        assertEquals(1, actualUpdateCategoryResult.getCategoryId().intValue());
    }

    /**
     * Method under test:
     * {@link CategoryServiceImpl#updateCategory(CategoryDto, Integer)}
     */
    @Test
    void testUpdateCategory2() {
        // Arrange
        when(categoryRepository.save(Mockito.<Category>any())).thenThrow(
                new ResourceNotFoundException("Updating category with Id {}: {}", "Updating category with Id {}: {}", 42L));
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(category));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> categoryServiceImpl.updateCategory(new CategoryDto(), 1));
        verify(categoryRepository).findById(eq(1));
        verify(categoryRepository).save(isA(Category.class));
    }

    /**
     * Method under test:
     * {@link CategoryServiceImpl#updateCategory(CategoryDto, Integer)}
     */
    @Test
    void testUpdateCategory3() {
        // Arrange
        Optional<Category> emptyResult = Optional.empty();
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> categoryServiceImpl.updateCategory(new CategoryDto(), 1));
        verify(categoryRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link CategoryServiceImpl#deleteCategory(Integer)}
     */
    @Test
    void testDeleteCategory() {
        // Arrange
        doNothing().when(categoryRepository).delete(Mockito.<Category>any());
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(category));
        // Act
        categoryServiceImpl.deleteCategory(1);
        // Assert that nothing has changed
        verify(categoryRepository).delete(isA(Category.class));
        verify(categoryRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link CategoryServiceImpl#deleteCategory(Integer)}
     */
    @Test
    void testDeleteCategory2() {
        // Arrange
        doThrow(new ResourceNotFoundException("deleting category with Id: {}", "deleting category with Id: {}", 42L))
                .when(categoryRepository)
                .delete(Mockito.<Category>any());
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(category));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> categoryServiceImpl.deleteCategory(1));
        verify(categoryRepository).delete(isA(Category.class));
        verify(categoryRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link CategoryServiceImpl#deleteCategory(Integer)}
     */
    @Test
    void testDeleteCategory3() {
        // Arrange
        Optional<Category> emptyResult = Optional.empty();
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> categoryServiceImpl.deleteCategory(1));
        verify(categoryRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link CategoryServiceImpl#getCategory(Integer)}
     */
    @Test
    void testGetCategory() {
        // Arrange
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(category));
        // Act
        CategoryDto actualCategory = categoryServiceImpl.getCategory(1);
        // Assert
        verify(categoryRepository).findById(eq(1));
        assertEquals("Category Description", actualCategory.getCategoryDescription());
        assertEquals("Dr", actualCategory.getCategoryTitle());
        assertEquals(1, actualCategory.getCategoryId().intValue());
    }

    /**
     * Method under test: {@link CategoryServiceImpl#getCategory(Integer)}
     */
    @Test
    void testGetCategory2() {
        // Arrange
        Optional<Category> emptyResult = Optional.empty();
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> categoryServiceImpl.getCategory(1));
        verify(categoryRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link CategoryServiceImpl#getCategory(Integer)}
     */
    @Test
    void testGetCategory3() {
        // Arrange
        when(categoryRepository.findById(Mockito.<Integer>any()))
                .thenThrow(new ResourceNotFoundException("category", "category", 42L));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> categoryServiceImpl.getCategory(1));
        verify(categoryRepository).findById(eq(1));
    }

    /**
     * Method under test:
     * {@link CategoryServiceImpl#getAllCategory(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllCategory() {
        // Arrange
        ArrayList<Category> content = new ArrayList<>();
        when(categoryRepository.findAll(Mockito.<Pageable>any())).thenReturn(new PageImpl<>(content));

        // Act
        CategoryResponse actualAllCategory = categoryServiceImpl.getAllCategory(1, 3, "Sort By", "Sort Dir");

        // Assert
        verify(categoryRepository).findAll(isA(Pageable.class));
        assertEquals(0, actualAllCategory.getPageNo());
        assertEquals(0, actualAllCategory.getPageSize());
        assertEquals(0L, actualAllCategory.getTotalElement());
        assertEquals(1, actualAllCategory.getTotalPages());
        assertTrue(actualAllCategory.isLastPage());
        assertEquals(content, actualAllCategory.getContent());
    }

    /**
     * Method under test:
     * {@link CategoryServiceImpl#getAllCategory(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllCategory2() {
        // Arrange
        Category category = new Category();
        category.setCategoryDescription("Fetching all category with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}");
        category.setCategoryId(1);
        category.setCategoryTitle("Dr");
        category.setPost(new ArrayList<>());

        ArrayList<Category> content = new ArrayList<>();
        content.add(category);
        PageImpl<Category> pageImpl = new PageImpl<>(content);
        when(categoryRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);
        // Act
        CategoryResponse actualAllCategory = categoryServiceImpl.getAllCategory(1, 3, "Sort By", "Sort Dir");
        // Assert
        verify(categoryRepository).findAll(isA(Pageable.class));
        List<CategoryDto> content2 = actualAllCategory.getContent();
        assertEquals(1, content2.size());
        CategoryDto getResult = content2.get(0);
        assertEquals("Dr", getResult.getCategoryTitle());
        assertEquals("Fetching all category with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}",
                getResult.getCategoryDescription());
        assertEquals(0, actualAllCategory.getPageNo());
        assertEquals(1, actualAllCategory.getPageSize());
        assertEquals(1, actualAllCategory.getTotalPages());
        assertEquals(1, getResult.getCategoryId().intValue());
        assertEquals(1L, actualAllCategory.getTotalElement());
        assertTrue(actualAllCategory.isLastPage());
    }

    /**
     * Method under test:
     * {@link CategoryServiceImpl#getAllCategory(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllCategory3() {
        // Arrange
        Category category = new Category();
        category.setCategoryDescription("Fetching all category with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}");
        category.setCategoryId(1);
        category.setCategoryTitle("Dr");
        category.setPost(new ArrayList<>());

        Category category2 = new Category();
        category2.setCategoryDescription("asc");
        category2.setCategoryId(2);
        category2.setCategoryTitle("Mr");
        category2.setPost(new ArrayList<>());

        ArrayList<Category> content = new ArrayList<>();
        content.add(category2);
        content.add(category);
        PageImpl<Category> pageImpl = new PageImpl<>(content);
        when(categoryRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);

        // Act
        CategoryResponse actualAllCategory = categoryServiceImpl.getAllCategory(1, 3, "Sort By", "Sort Dir");

        // Assert
        verify(categoryRepository).findAll(isA(Pageable.class));
        List<CategoryDto> content2 = actualAllCategory.getContent();
        assertEquals(2, content2.size());
        CategoryDto getResult = content2.get(1);
        assertEquals("Dr", getResult.getCategoryTitle());
        assertEquals("Fetching all category with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}",
                getResult.getCategoryDescription());
        CategoryDto getResult2 = content2.get(0);
        assertEquals("Mr", getResult2.getCategoryTitle());
        assertEquals("asc", getResult2.getCategoryDescription());
        assertEquals(0, actualAllCategory.getPageNo());
        assertEquals(1, actualAllCategory.getTotalPages());
        assertEquals(1, getResult.getCategoryId().intValue());
        assertEquals(2, actualAllCategory.getPageSize());
        assertEquals(2, getResult2.getCategoryId().intValue());
        assertEquals(2L, actualAllCategory.getTotalElement());
        assertTrue(actualAllCategory.isLastPage());
    }

    /**
     * Method under test:
     * {@link CategoryServiceImpl#getAllCategory(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllCategory4() {
        // Arrange
        ArrayList<Category> content = new ArrayList<>();
        when(categoryRepository.findAll(Mockito.<Pageable>any())).thenReturn(new PageImpl<>(content));

        // Act
        CategoryResponse actualAllCategory = categoryServiceImpl.getAllCategory(1, 3, "Sort By", "asc");

        // Assert
        verify(categoryRepository).findAll(isA(Pageable.class));
        assertEquals(0, actualAllCategory.getPageNo());
        assertEquals(0, actualAllCategory.getPageSize());
        assertEquals(0L, actualAllCategory.getTotalElement());
        assertEquals(1, actualAllCategory.getTotalPages());
        assertTrue(actualAllCategory.isLastPage());
        assertEquals(content, actualAllCategory.getContent());
    }

    /**
     * Method under test:
     * {@link CategoryServiceImpl#getAllCategory(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllCategory5() {
        // Arrange
        when(categoryRepository.findAll(Mockito.<Pageable>any())).thenThrow(
                new ResourceNotFoundException("Fetching all category with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}",
                        "Fetching all category with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}", 42L));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class,
                () -> categoryServiceImpl.getAllCategory(1, 3, "Sort By", "Sort Dir"));
        verify(categoryRepository).findAll(isA(Pageable.class));
    }
}
