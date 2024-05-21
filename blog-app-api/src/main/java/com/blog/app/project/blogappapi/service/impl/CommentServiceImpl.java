package com.blog.app.project.blogappapi.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blog.app.project.blogappapi.dto.CommentDto;
import com.blog.app.project.blogappapi.entity.Comment;
import com.blog.app.project.blogappapi.entity.Post;
import com.blog.app.project.blogappapi.entity.User;
import com.blog.app.project.blogappapi.exception.ResourceNotFoundException;
import com.blog.app.project.blogappapi.payload.CommentResponse;
import com.blog.app.project.blogappapi.repository.CommentRepository;
import com.blog.app.project.blogappapi.repository.PostRepository;
import com.blog.app.project.blogappapi.repository.UserRepository;
import com.blog.app.project.blogappapi.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private PostRepository postRepository;
	@Autowired
	private UserRepository userRepository;

	@Override
	public CommentDto createComment(CommentDto commentDto, Integer userId, Integer postId) {
		logger.info("creating comment: {} {} {}", commentDto, userId, postId);
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("user", "userId", userId));

		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("post", "postId", postId));

//        Comment comment = modelMapper.map(commentDto, Comment.class);
		Comment comment = new Comment();
		comment = Comment.builder().id(commentDto.getId()).content(commentDto.getContent()).build();
		comment.setPost(post);
		comment.setUser(user);
		Comment savedComment = commentRepository.save(comment);
		logger.info("created comment: {}", savedComment);
		return commentDto = CommentDto.builder().id(comment.getId()).content(comment.getContent()).build();
	}

	@Override
	public void deleteComment(Integer commentId) {
		logger.info("deleting comment: {}", commentId);
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("comment", "commentId", commentId));
		logger.info("comment deleted: {}", comment);
		commentRepository.delete(comment);
	}

	@Override
	public CommentDto getComment(Integer commentId) {
		CommentDto commentDto = null;
		logger.info("getting comment: {}", commentId);
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("comment", "commentId", commentId));
		logger.debug("got comment: {}", comment);
		return commentDto = CommentDto.builder().id(comment.getId()).content(comment.getContent()).build();
	}

	@Override
	public CommentResponse getAllComment(Integer pageNo, Integer pageSize, String sortBy, String sortDir) {
		logger.info("Fetching all comments with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}", pageNo, pageSize,
				sortBy, sortDir);
		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		Page<Comment> pagePost = commentRepository.findAll(pageable);
		List<Comment> content = pagePost.getContent();
		List<Comment> all = commentRepository.findAll();
		
		List<CommentDto> collect = content.stream().map(comment->CommentDto.builder().id(comment.getId()).content(comment.getContent()).build())
				.collect(Collectors.toList());
		
		CommentResponse commentResponse = new CommentResponse();
		commentResponse.setContent(collect);
		commentResponse.setPageNo(pagePost.getNumber());
		commentResponse.setPageSize(pagePost.getSize());
		commentResponse.setTotalElement(pagePost.getTotalElements());
		commentResponse.setTotalPages(pagePost.getTotalPages());
		commentResponse.setLastPage(pagePost.isLast());
		logger.debug("getting all comments: {}", commentResponse);
		return commentResponse;
	}

	@Override
	public CommentDto updateComment(int commentId, CommentDto commentDto) {
		logger.info("updating comment: {} {}", commentId, commentDto);
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("comment", "commentId", commentId));
		comment.setContent(commentDto.getContent());
		Comment save = commentRepository.save(comment);
		logger.info("updated comment: {}", save);
		return CommentDto.builder().id(comment.getId()).content(comment.getContent()).build();
	}
}
