package com.blog.app.project.blogappapi.controller;

import com.blog.app.project.blogappapi.dto.CategoryDto;
import com.blog.app.project.blogappapi.entity.Category;
import com.blog.app.project.blogappapi.payload.ApiResponse;
import com.blog.app.project.blogappapi.payload.AppConstantsCategory;
import com.blog.app.project.blogappapi.payload.CategoryResponse;
import com.blog.app.project.blogappapi.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "Create Category")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "category created",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class)) })})
    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDto> crateCategory(@Valid @RequestBody CategoryDto categoryDto){
        CategoryDto category = categoryService.createCategory(categoryDto);
        return new ResponseEntity<CategoryDto>(category, HttpStatus.CREATED);
    }

    @Operation(summary = "Update Category By Its Id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "category updated successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content) })
    @PutMapping(value = "/{categoryId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDto> updateCategory(
            @Valid @RequestBody CategoryDto categoryDto,
            @PathVariable Integer categoryId
    ){
        CategoryDto updateCat = categoryService.updateCategory(categoryDto, categoryId);
        return new ResponseEntity<CategoryDto>(updateCat,HttpStatus.OK);
    }


    @Operation(summary = "Delete Category By Its Id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category deleted",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content) })
    @DeleteMapping(value = "/{categoryId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer categoryId){
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<ApiResponse>(new ApiResponse("category is deleted",true),HttpStatus.OK);
    }

    @Operation(summary = "Get Category By Its Id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Found the category",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content) })
    @GetMapping(value = "/{categoryId}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Integer categoryId){
        CategoryDto category = categoryService.getCategory(categoryId);
        return new ResponseEntity<CategoryDto>(category,HttpStatus.OK);
    }

    @Operation(summary = "Get All Categories")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Found the categories",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Categories not found")
    })
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(value = "pageNo", defaultValue = AppConstantsCategory.PAGE_NO, required = false) Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstantsCategory.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstantsCategory.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstantsCategory.SORT_DIR, required = false) String sortDir
    ){
        CategoryResponse allCategory = categoryService.getAllCategory(pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<CategoryResponse>(allCategory,HttpStatus.OK);
    }
}
