package com.blog.app.project.blogappapi.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blog.app.project.blogappapi.dto.AddressDto;
import com.blog.app.project.blogappapi.dto.CategoryDto;
import com.blog.app.project.blogappapi.dto.CommentDto;
import com.blog.app.project.blogappapi.dto.PostDto;
import com.blog.app.project.blogappapi.dto.UserDto;
import com.blog.app.project.blogappapi.entity.Category;
import com.blog.app.project.blogappapi.entity.Comment;
import com.blog.app.project.blogappapi.entity.Post;
import com.blog.app.project.blogappapi.entity.User;
import com.blog.app.project.blogappapi.exception.ResourceNotFoundException;
import com.blog.app.project.blogappapi.openfeignclient.AddressClient;
import com.blog.app.project.blogappapi.payload.PostResponse;
import com.blog.app.project.blogappapi.repository.CategoryRepository;
import com.blog.app.project.blogappapi.repository.PostRepository;
import com.blog.app.project.blogappapi.repository.UserRepository;
import com.blog.app.project.blogappapi.service.PostService;

import feign.FeignException;

@Service
public class PostServiceImpl implements PostService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private AddressClient addressClient;

	@Override
	public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {
		logger.info("Creating post: {} {} {}", postDto, userId, categoryId);
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("user", "UserId", userId));

		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("category", "categoryId", categoryId));

		AddressDto addressDto = null;
		UserDto getUserFromPost = postDto.getUser();
		if (getUserFromPost != null) {
			try {
				addressDto = addressClient.getAddressByUserId(userId);
				getUserFromPost.setAddressDto(addressDto);
			} catch (FeignException.NotFound ex) {
				String errorMessage = String.format("address not found with UserId: %d", userId);
				logger.error(errorMessage);
				getUserFromPost.setAddressDto(null);
			}
		}

		CategoryDto categoryDto = CategoryDto.builder().categoryId(category.getCategoryId())
				.categoryTitle(category.getCategoryTitle()).categoryDescription(category.getCategoryDescription())
				.build();

		UserDto userDto = UserDto.builder().id(user.getId()).name(user.getName()).email(user.getEmail())
				.password(user.getPassword()).about(user.getAbout()).addressDto(addressDto).build();

		Post post = Post.builder().postId(postDto.getPostId()).title(postDto.getTitle()).content(postDto.getContent())
				.addDate(postDto.getAddedDate()).imageName(postDto.getImageName()).category(category).comments(null)
				.user(user).build();
		post.setImageName("def.png");
		post.setAddDate(new Date());
		post.setUser(user);
		post.setCategory(category);
		Post savePost = postRepository.save(post);
		logger.info("created post: {}", savePost);

		return PostDto.builder().postId(post.getPostId()).title(post.getTitle()).content(post.getContent())
				.imageName(post.getImageName()).addedDate(post.getAddDate()).category(categoryDto).user(userDto)
				.build();
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {
		logger.info("updating post: {} {}", postDto, postId);
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("post", "postId", postId));
		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setImageName(postDto.getImageName());
		Post save = postRepository.save(post);
		logger.info("post updated: {} ", save);

		AddressDto addressDto = null;
		UserDto userDtoGetUser = postDto.getUser();
		if (userDtoGetUser != null) {
			int userId = postDto.getUser().getId();
			try {
				addressDto = addressClient.getAddressByUserId(userId);
				userDtoGetUser.setAddressDto(addressDto);
			} catch (FeignException.NotFound ex) {
				String errorMessage = String.format("address not found with UserId: %d", userId);
				logger.error(errorMessage);
				userDtoGetUser.setAddressDto(null);
			}
		}

		Set<CommentDto> commentsList = new HashSet<>();
		for (Comment comment : post.getComments()) {
			commentsList.add(CommentDto.builder().id(comment.getId()).content(comment.getContent()).build());
		}

		CategoryDto categoryDto = CategoryDto.builder().categoryId(post.getCategory().getCategoryId())
				.categoryTitle(post.getCategory().getCategoryTitle())
				.categoryDescription(post.getCategory().getCategoryDescription()).build();

		UserDto userDto = UserDto.builder().id(post.getUser().getId()).name(post.getUser().getName())
				.email(post.getUser().getEmail()).password(post.getUser().getPassword())
				.about(post.getUser().getAbout()).addressDto(addressDto).build();

		return PostDto.builder().postId(post.getPostId()).title(post.getTitle()).content(post.getContent())
				.imageName(post.getImageName()).addedDate(post.getAddDate()).category(categoryDto).user(userDto)
				.comments(commentsList).build();

	}

	@Override
	public void deletePost(Integer postId) {
		logger.info("deleting post: {}", postId);
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("post", "postId", postId));
		postRepository.delete(post);
		logger.info("post deleted successfully...");
	}

	@Override
	public PostDto getPostById(Integer postId) {
		logger.info("get post by Id: {}", postId);
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("post", "postId", postId));
		logger.debug("post found: {}", post);

		Integer userId = post.getUser().getId();
		AddressDto addressDto = null;
		try {
			addressDto = addressClient.getAddressByUserId(userId);
		} catch (FeignException.NotFound ex) {
			String errorMessage = String.format("address not found with UserId: %d", userId);
			logger.error(errorMessage);
		}

		Set<CommentDto> commentList = new HashSet<>();
		for (Comment comment : post.getComments()) {
			commentList.add(CommentDto.builder().id(comment.getId()).content(comment.getContent()).build());
		}

		CategoryDto categoryDto = CategoryDto.builder().categoryId(post.getCategory().getCategoryId())
				.categoryTitle(post.getCategory().getCategoryTitle())
				.categoryDescription(post.getCategory().getCategoryDescription()).build();

		UserDto userDto = UserDto.builder().id(post.getUser().getId()).name(post.getUser().getName())
				.email(post.getUser().getEmail()).password(post.getUser().getPassword())
				.about(post.getUser().getAbout()).addressDto(addressDto).build();

		PostDto postDto = PostDto.builder().postId(post.getPostId()).title(post.getTitle()).content(post.getContent())
				.imageName(post.getImageName()).addedDate(post.getAddDate()).category(categoryDto).user(userDto)
				.comments(commentList).build();

		userDto = postDto.getUser();
		if (userDto != null) {
			userDto.setAddressDto(addressDto);
		}
		return postDto;
	}

	@Override
	public PostResponse getAllPost(Integer pageNo, Integer pageSize, String sortBy, String sortDir) {
		logger.info("Fetching all posts with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}", pageNo, pageSize,
				sortBy, sortDir);
		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		Page<Post> pagePost = postRepository.findAll(pageable);
		List<Post> content = pagePost.getContent();

		List<PostDto> collect = content.stream()
				.map(post -> PostDto.builder().postId(post.getPostId()).title(post.getTitle())
						.content(post.getContent()).imageName(post.getImageName()).addedDate(post.getAddDate())
						.category(CategoryDto.builder().categoryId(post.getCategory().getCategoryId())
								.categoryTitle(post.getCategory().getCategoryTitle())
								.categoryDescription(post.getCategory().getCategoryDescription()).build())
						.user(UserDto.builder().id(post.getUser().getId()).name(post.getUser().getName())
								.email(post.getUser().getEmail()).password(post.getUser().getPassword())
								.about(post.getUser().getAbout()).addressDto(null).build())
						.comments(
								post.getComments().stream()
										.map(comment -> CommentDto.builder().id(comment.getId())
												.content(comment.getContent()).build())
										.collect(Collectors.toSet()))
						.build())
				.collect(Collectors.toList());

		List<PostDto> postDtoList = new ArrayList<>();

		for (PostDto postDto : collect) {
			int userId = postDto.getUser().getId();
			AddressDto addressDto = null;
			try {
				addressDto = addressClient.getAddressByUserId(userId);
			} catch (FeignException.NotFound ex) {
				logger.error("address not found for User with id: {}", userId);
			}

			postDto.getUser().setAddressDto(addressDto);
			postDtoList.add(postDto);
		}

		PostResponse postResponse = new PostResponse();
		postResponse.setContent(postDtoList);
		postResponse.setPageNo(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalElement(pagePost.getTotalElements());
		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setLastPage(pagePost.isLast());
		logger.info("fetched all post response: {}", postResponse);
		return postResponse;
	}

	@Override
	public List<PostDto> getPostsByCategory(Integer categoryId) {
		logger.info("getting post by category: {}", categoryId);
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));
		List<Post> byCategory = postRepository.findByCategory(category);

		List<PostDto> collect = byCategory.stream()
				.map(post -> PostDto.builder().postId(post.getPostId()).title(post.getTitle())
						.content(post.getContent()).imageName(post.getImageName()).addedDate(post.getAddDate())
						.category(CategoryDto.builder().categoryId(post.getCategory().getCategoryId())
								.categoryTitle(post.getCategory().getCategoryTitle())
								.categoryDescription(post.getCategory().getCategoryDescription()).build())
						.user(UserDto.builder().id(post.getUser().getId()).name(post.getUser().getName())
								.email(post.getUser().getEmail()).password(post.getUser().getPassword())
								.about(post.getUser().getAbout()).addressDto(null).build())
						.comments(
								post.getComments().stream()
										.map(comment -> CommentDto.builder().id(comment.getId())
												.content(comment.getContent()).build())
										.collect(Collectors.toSet()))
						.build())
				.collect(Collectors.toList());

		List<PostDto> postDtoList = new ArrayList<>();

		for (PostDto postDto : collect) {
			int userId = postDto.getUser().getId();
			AddressDto addressDto = null;
			try {
				addressDto = addressClient.getAddressByUserId(userId);
			} catch (FeignException.NotFound ex) {
				logger.error("address not found for User with id: {}", userId);
			}

			postDto.getUser().setAddressDto(addressDto);
			postDtoList.add(postDto);
		}
		logger.info("got post by category: {}", collect);
		return postDtoList;
	}

	@Override
	public List<PostDto> getPostsByUser(Integer userId) {
		logger.info("getting post by user: {}", userId);
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "UserId", userId));
		List<Post> byUser = postRepository.findByUser(user);

		List<PostDto> collect = byUser.stream()
				.map(post -> PostDto.builder().postId(post.getPostId()).title(post.getTitle())
						.content(post.getContent()).imageName(post.getImageName()).addedDate(post.getAddDate())
						.category(CategoryDto.builder().categoryId(post.getCategory().getCategoryId())
								.categoryTitle(post.getCategory().getCategoryTitle())
								.categoryDescription(post.getCategory().getCategoryDescription()).build())
						.user(UserDto.builder().id(post.getUser().getId()).name(post.getUser().getName())
								.email(post.getUser().getEmail()).password(post.getUser().getPassword())
								.about(post.getUser().getAbout()).addressDto(null).build())
						.comments(
								post.getComments().stream()
										.map(comment -> CommentDto.builder().id(comment.getId())
												.content(comment.getContent()).build())
										.collect(Collectors.toSet()))
						.build())
				.collect(Collectors.toList());

		List<PostDto> postDtoList = new ArrayList<>();

		for (PostDto postDto : collect) {
			AddressDto addressDto = null;
			try {
				addressDto = addressClient.getAddressByUserId(userId);
			} catch (FeignException.NotFound ex) {
				logger.error("address not found for User with id: {}", userId);
			}
			postDto.getUser().setAddressDto(addressDto);
			postDtoList.add(postDto);
		}
		logger.info("got post by user: {}", collect);
		return postDtoList;
	}

	@Override
	public List<PostDto> searchPosts(String keyword) {
		logger.info("searching posts: {}", keyword);
		List<Post> posts = postRepository.findByTitleContaining(keyword);

		List<PostDto> collect = posts.stream()
				.map(post -> PostDto.builder().postId(post.getPostId()).title(post.getTitle())
						.content(post.getContent()).imageName(post.getImageName()).addedDate(post.getAddDate())
						.category(CategoryDto.builder().categoryId(post.getCategory().getCategoryId())
								.categoryTitle(post.getCategory().getCategoryTitle())
								.categoryDescription(post.getCategory().getCategoryDescription()).build())
						.user(UserDto.builder().id(post.getUser().getId()).name(post.getUser().getName())
								.email(post.getUser().getEmail()).password(post.getUser().getPassword())
								.about(post.getUser().getAbout()).addressDto(null).build())
						.comments(
								post.getComments().stream()
										.map(comment -> CommentDto.builder().id(comment.getId())
												.content(comment.getContent()).build())
										.collect(Collectors.toSet()))
						.build())
				.collect(Collectors.toList());

		logger.info("post searchred: {}", collect);

		List<PostDto> postDtoList = new ArrayList<>();

		for (PostDto postDto : collect) {
			int userId = postDto.getUser().getId();
			AddressDto addressDto = null;
			try {
				addressDto = addressClient.getAddressByUserId(userId);
			} catch (FeignException.NotFound ex) {
				logger.error("address not found for User with id: {}", userId);
			}

			postDto.getUser().setAddressDto(addressDto);
			postDtoList.add(postDto);
		}
		return postDtoList;
	}

	@Override
	public List<UserDto> getUsersWhoCommented(int postId) {
		List<User> userByCommentPostId = postRepository.findUsersByCommentsPostId(postId);

		List<UserDto> collect = userByCommentPostId.stream()
				.map(user -> UserDto.builder().id(user.getId()).name(user.getName()).email(user.getEmail())
						.password(user.getPassword()).about(user.getAbout()).addressDto(null).build())
				.collect(Collectors.toList());

		List<UserDto> userDtoList = new ArrayList<>();

		for (UserDto userDto : collect) {	
			int userId = userDto.getId();
			AddressDto addressDto = null;
			try {
				addressDto = addressClient.getAddressByUserId(userId);
			} catch (FeignException.NotFound ex) {
				logger.error("address not found for User with id: {}", userId);
			}

			userDto.setAddressDto(addressDto);
			userDtoList.add(userDto);
		}

		return userDtoList;
	}
}
