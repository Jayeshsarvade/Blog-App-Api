package com.blog.app.project.blogappapi.repository;

import com.blog.app.project.blogappapi.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
