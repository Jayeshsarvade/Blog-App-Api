package com.blog.app.project.blogappapi.controller;

import com.blog.app.project.blogappapi.dto.UserDto;
import com.blog.app.project.blogappapi.entity.User;
import com.blog.app.project.blogappapi.payload.ApiResponse;
import com.blog.app.project.blogappapi.payload.AppConstantsUser;
import com.blog.app.project.blogappapi.payload.UserResponse;
import com.blog.app.project.blogappapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This class is a REST controller for handling user-related operations.
 * It provides endpoints for creating, updating, retrieving, and deleting users.
 * It also provides an endpoint for retrieving all users with pagination and sorting.
 */

@RestController
@RequestMapping("/api/users")
public class UserController {

    /**
     * The UserService instance that provides the business logic for user operations.
     */

    @Autowired
    private UserService userService;

    /**
     * This method is used to create a new user.
     *
     * @param userDto The user data to be created. This data should be validated and sanitized before being passed to this method.
     * @return A ResponseEntity containing the created user data and a HTTP status code of 201 (Created).
     * @throws IllegalArgumentException If the userDto is null or invalid.
     */

    @Operation(summary = "Create User")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User Created",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) })})
    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
        UserDto user = userService.createUser(userDto);
        return new ResponseEntity<UserDto>(user, HttpStatus.CREATED);
    }

    /**
     * This method is used to update an existing user by its id.
     *
     * @param userDto The user data to be updated. This data should be validated and sanitized before being passed to this method.
     * @param userId The id of the user to be updated. This id should be a valid integer and not null.
     * @return A ResponseEntity containing the updated user data and a HTTP status code of 200 (OK).
     * @throws IllegalArgumentException If the userDto is null or invalid, or if the userId is null or invalid.
     * @throws ChangeSetPersister.NotFoundException If the user with the given id is not found in the database.
     */

    @Operation(summary = "Update User By Its Id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User Updated Successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content) })
    @PutMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable Integer userId){
        UserDto updatedUSer = userService.updateUSer(userDto, userId);
        return new ResponseEntity<>(updatedUSer,HttpStatus.OK);
    }

    /**
     * This method is used to get a user by its id.
     *
     * @param userId The id of the user to be retrieved. This id should be a valid integer and not null.
     * @return A ResponseEntity containing the user data and a HTTP status code of 200 (OK).
     * @throws IllegalArgumentException If the userId is null or invalid.
     * @throws ChangeSetPersister.NotFoundException If the user with the given id is not found in the database.
     */

    @Operation(summary = "Get User By Its Id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Found the User",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content) })
    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> getUser(@PathVariable Integer userId){
        UserDto getUser = userService.getUser(userId);
        return new ResponseEntity<>(getUser,HttpStatus.OK);
    }

    /**
     * This method is used to delete a user by its id.
     *
     * @param userId The id of the user to be deleted. This id should be a valid integer and not null.
     * @return A ResponseEntity containing an ApiResponse with a success message and a HTTP status code of 200 (OK).
     * @throws IllegalArgumentException If the userId is null or invalid.
     * @throws ChangeSetPersister.NotFoundException If the user with the given id is not found in the database.
     */

    @Operation(summary = "Delete User By Its Id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User Deleted",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content) })
    @DeleteMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer userId){
        userService.deleteUser(userId);
        return new ResponseEntity<>(new ApiResponse("user deleted Successfully...", true), HttpStatus.OK);
    }

    /**
     * This method retrieves all users from the database.
     *
     * @param pageNo The page number for pagination. Default value is defined in AppConstantsUser.PAGE_NO.
     * @param pageSize The number of users per page for pagination. Default value is defined in AppConstantsUser.PAGE_SIZE.
     * @param sortBy The field to sort the users by. Default value is defined in AppConstantsUser.SORT_BY.
     * @param sortDir The direction to sort the users. Default value is defined in AppConstantsUser.SORT_DIR.
     * @return A ResponseEntity containing a UserResponse object with the retrieved users and a HTTP status code of 200 (OK).
     * @throws ChangeSetPersister.NotFoundException If no users are found in the database.
     */

    @Operation(summary = "Get All Users")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Found the Users",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found") })
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> getAllUsers(
            @RequestParam(value = "pageNo", defaultValue = AppConstantsUser.PAGE_NO, required = false) Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstantsUser.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstantsUser.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstantsUser.SORT_DIR, required = false) String sortDir
    ){
        UserResponse userResponse = userService.gatAllUser(pageNo, pageSize,sortBy,sortDir);
        return new ResponseEntity<UserResponse>(userResponse,HttpStatus.OK);
    }
}

