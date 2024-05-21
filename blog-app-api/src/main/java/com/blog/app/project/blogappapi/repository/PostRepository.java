package com.blog.app.project.blogappapi.repository;

import com.blog.app.project.blogappapi.entity.Category;
import com.blog.app.project.blogappapi.entity.Post;
import com.blog.app.project.blogappapi.entity.User;

import feign.Param;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer>{

    List<Post> findByUser(User user);
    List<Post> findByCategory(Category category);
    List<Post> findByTitleContaining(String title);


    @Query("SELECT DISTINCT c.user FROM Comment c WHERE c.post.id = :postId")
    List<User> findUsersByCommentsPostId(@Param("postId") int postId);
}
