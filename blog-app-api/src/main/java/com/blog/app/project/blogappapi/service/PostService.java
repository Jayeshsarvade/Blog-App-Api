package com.blog.app.project.blogappapi.service;

import com.blog.app.project.blogappapi.dto.PostDto;
import com.blog.app.project.blogappapi.dto.UserDto;
import com.blog.app.project.blogappapi.payload.PostResponse;

import java.util.List;

public interface PostService {

    public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId);

    public PostDto updatePost(PostDto postDto, Integer postId);

    public void deletePost(Integer postId);

    public PostDto getPostById(Integer postId);

    PostResponse getAllPost(Integer pageNo, Integer pageSize, String sortBy, String sortDir);

    List<PostDto> getPostsByCategory(Integer categoryId);

    List<PostDto> getPostsByUser(Integer userId);

    List<PostDto> searchPosts(String keyword);
    
    List<UserDto> getUsersWhoCommented(int postId);
}
