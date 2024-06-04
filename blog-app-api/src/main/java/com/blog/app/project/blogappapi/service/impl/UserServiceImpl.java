package com.blog.app.project.blogappapi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blog.app.project.blogappapi.dto.AddressDto;
import com.blog.app.project.blogappapi.dto.UserDto;
import com.blog.app.project.blogappapi.entity.User;
import com.blog.app.project.blogappapi.exception.ResourceNotFoundException;
import com.blog.app.project.blogappapi.openfeignclient.AddressClient;
import com.blog.app.project.blogappapi.payload.UserResponse;
import com.blog.app.project.blogappapi.repository.UserRepository;
import com.blog.app.project.blogappapi.service.UserService;

import feign.FeignException;
import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Value("${addressservice.base.url}")
	private String addressBaseUrl;

	@Autowired
	private AddressClient addressClient;

	/**
	 * This method creates a new user in the system.
	 *
	 * @param userdto The DTO object containing the details of the user to be created.
	 * @return A UserDto object containing the details of the newly created user.
	 * @throws IllegalArgumentException If the userDto object is null or any of its required fields are missing.
	 */

	@Override
	public UserDto createUser(UserDto userdto) {
		logger.info("Creating user: {}", userdto);
		User user = User.builder().id(userdto.getId()).name(userdto.getName())
				.email(userdto.getEmail()).password(userdto.getPassword())
				.about(userdto.getAbout()).build();
		User saveUser = userRepository.save(user);
		logger.info("User created successfully: {}", saveUser);
		return UserDto.builder().id(user.getId()).name(user.getName())
				.email(user.getEmail()).password(user.getPassword())
				.about(user.getAbout()).build();
	}

	/**
	 * This method updates a user in the system.
	 *
	 * @param userDto The DTO object containing the updated details of the user.
	 * @param userId The unique identifier of the user to be updated.
	 * @return A UserDto object containing the updated details of the user.
	 * @throws FeignException If an error occurs while deleting the address associated with the user.
	 * @throws ResourceNotFoundException If the user with the given userId is not found.
	 */

	@Override
	public UserDto updateUSer(UserDto userDto, Integer userId) {
		logger.info("Updating user with Id {}: {}", userId, userDto);
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("user", "userId", userId));
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());
		user.setAbout(userDto.getAbout());
		User save = userRepository.save(user);
		logger.info("User updated successfully: {}", save);
		
		AddressDto addressByUserId = null;
		try {
			addressByUserId = addressClient.getAddressByUserId(userId);
		}catch (FeignException.NotFound ex) {
			String errorMessage = String.format("address not found with UserId: %d", userId);
			logger.error(errorMessage);
			userDto.setAddressDto(null);
		}
		UserDto userDto2 = UserDto.builder().id(user.getId()).name(user.getName()).email(user.getEmail())
				.password(user.getPassword()).about(user.getAbout()).build();
		userDto2.setAddressDto(addressByUserId);
		return userDto2;
	}

	/**
	 * This method retrieves a user by their unique identifier.
	 *
	 * @param userId The unique identifier of the user to be fetched.
	 * @return A UserDto object containing the details of the fetched user.
	 * @throws FeignException If an error occurs while deleting the address associated with the user.
	 * @throws ResourceNotFoundException If the user with the given userId is not found.
	 */

	@Override
	public UserDto getUser(Integer userId) {
		logger.info("Fetching user with Id: {}", userId);
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("user", "userId", userId));
		logger.debug("User found: {}", user);
		UserDto userDto = new UserDto();
		userDto = UserDto.builder().id(user.getId()).name(user.getName()).email(user.getEmail())
				.password(user.getPassword()).about(user.getAbout()).build();
		try {
			AddressDto addressDto = addressClient.getAddressByUserId(userId);
			userDto.setAddressDto(addressDto);
		} catch (FeignException.NotFound ex) {
			String errorMessage = String.format("address not found with UserId: %d", userId);
			logger.error(errorMessage);
			userDto.setAddressDto(null);
		}
		return userDto;
	}


	/**
	 * This method retrieves all users from the system with pagination, sorting, and filtering.
	 *
	 * @param pageNo The page number to retrieve.
	 * @param pageSize The number of users to retrieve per page.
	 * @param sortBy The field to sort the users by.
	 * @param sortDir The direction to sort the users (asc or desc).
	 * @return A UserResponse object containing the details of the retrieved users.
	 */
	@Override
	public UserResponse gatAllUser(Integer pageNo, Integer pageSize, String sortBy, String sortDir) {
		logger.info("Fetching all users with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}", pageNo, pageSize,
				sortBy, sortDir);
		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		Page<User> pagePost = userRepository.findAll(pageable);
		List<User> content = pagePost.getContent();

		List<UserDto> userDtoList = new ArrayList<>();

		for (User user : content) {
			AddressDto addressDto = null;
			try {
				addressDto = addressClient.getAddressByUserId(user.getId());
			} catch (FeignException.NotFound ex) {
				logger.error("address not found for user with Id: {}", user.getId());
			}
			UserDto userDto = new UserDto();
			userDto= UserDto.builder().id(user.getId()).name(user.getName()).email(user.getEmail())
					.password(user.getPassword()).about(user.getAbout()).build();
			userDto.setAddressDto(addressDto);
			userDtoList.add(userDto);
		}

		UserResponse userResponse = new UserResponse();
		userResponse.setContent(userDtoList);
		userResponse.setPageNo(pagePost.getNumber());
		userResponse.setPageSize(pagePost.getSize());
		userResponse.setTotalElement(pagePost.getTotalElements());
		userResponse.setTotalPages(pagePost.getTotalPages());
		userResponse.setLastPage(pagePost.isLast());
		logger.info("Returning user response: {}", userResponse.toString());
		return userResponse;
	}


	/**
	 * This method deletes a user in the system.
	 *
	 * @param userId The unique identifier of the user to be deleted.
	 * @throws ResourceNotFoundException If the user with the given userId is not found.
	 * @throws FeignException If an error occurs while deleting the address associated with the user.
	 */

	@Override
	@Transactional
	public void deleteUser(Integer userId) {
		logger.info("Deleting user with Id: {}", userId);
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("user", "userId", userId));
		try {
            addressClient.deleteAddressByUserId(userId);
        } catch (FeignException.NotFound ex) {
            logger.warn("Address not found for user with Id: {}, proceeding with user deletion", userId);
        } catch (FeignException ex) {
            logger.error("Error occurred while deleting address for user with Id: {}", userId, ex);
            throw ex;
        }
        userRepository.delete(user);

        logger.info("User deleted successfully...");
	}
}
