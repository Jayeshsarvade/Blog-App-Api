package com.blog.app.project.blogappapi.repository;

import com.blog.app.project.blogappapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

}
