package com.blog.app.project.blogappapi.service;

import com.blog.app.project.blogappapi.dto.CommentDto;
import com.blog.app.project.blogappapi.payload.CommentResponse;

public interface CommentService {

    public CommentDto createComment(CommentDto commentDto, Integer userId, Integer postId);
    void deleteComment(Integer commentId);
    public CommentDto getComment(Integer commentId);
    public CommentResponse getAllComment(Integer pageNo, Integer pageSize, String sortBy, String sortDir);
    public CommentDto updateComment(int commentId, CommentDto commentDto);
}
