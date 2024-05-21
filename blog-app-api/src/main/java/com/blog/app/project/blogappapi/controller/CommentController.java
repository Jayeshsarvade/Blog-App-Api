package com.blog.app.project.blogappapi.controller;

import com.blog.app.project.blogappapi.dto.CommentDto;
import com.blog.app.project.blogappapi.entity.Comment;
import com.blog.app.project.blogappapi.payload.ApiResponse;
import com.blog.app.project.blogappapi.payload.AppConstantsComment;
import com.blog.app.project.blogappapi.payload.CommentResponse;
import com.blog.app.project.blogappapi.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/")
public class CommentController {

    @Autowired
    CommentService commentService;

    @Operation(summary = "Create Comment")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Comment created successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid user id or post id supplied",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User id or post id not found",
                    content = @Content) })
    @PostMapping(value = "/user/{userId}/post/{postId}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentDto> createComment(
            @Valid @RequestBody CommentDto commentDto,
            @PathVariable Integer userId,
            @PathVariable Integer postId
    ){
        CommentDto comment = commentService.createComment(commentDto,userId, postId);
        return new ResponseEntity<CommentDto>(comment, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete Comment By Its Id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Comment deleted successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Comment not found",
                    content = @Content) })
    @DeleteMapping(value = "/comment/{commentId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> deleteComment(
            @PathVariable Integer commentId
    ){
        commentService.deleteComment(commentId);
        return  new ResponseEntity<ApiResponse>(new ApiResponse("comment deleted Successfully...", true), HttpStatus.OK);
    }

    @Operation(summary = "Get Comment By Its Id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Found the comment",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Comment not found",
                    content = @Content) })
    @GetMapping(value = "/comment/{commentId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentDto> getComment(@PathVariable Integer commentId){
        CommentDto comment = commentService.getComment(commentId);
        return new ResponseEntity<CommentDto>(comment, HttpStatus.OK);
    }

    @Operation(summary = "Get All Comments")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Found the comments",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "comment not found") })
    @GetMapping(value = "/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentResponse> getAllComments(
            @RequestParam(value = "pageNo", defaultValue = AppConstantsComment.PAGE_NO, required = false) Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstantsComment.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstantsComment.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstantsComment.SORT_DIR,required = false) String sortDir
    ){
        CommentResponse allComment = commentService.getAllComment(pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<CommentResponse>(allComment, HttpStatus.OK);
    }

    

    @Operation(summary = "update comments by its Id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "comment updated successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "comment not found") })
    @PutMapping(value = "/comment/{id}")
    public ResponseEntity<CommentDto> updateComment(@Valid @PathVariable int id,@RequestBody CommentDto commentDto){
        CommentDto updatedComment = commentService.updateComment(id, commentDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedComment);
    }
}
