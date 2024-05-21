package com.blog.app.project.blogappapi.service;

import com.blog.app.project.blogappapi.dto.UserDto;
import com.blog.app.project.blogappapi.payload.UserResponse;

public interface UserService {

    public UserDto createUser(UserDto userdto);

    public UserDto updateUSer(UserDto userDto, Integer id);

    public UserDto getUser(Integer userId);

    public UserResponse gatAllUser(Integer pageNo, Integer pageSize, String sortBy, String sortDir);

    public void deleteUser(Integer userId);
}
