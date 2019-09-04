package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
	private UserController userController;

	@Mock
	private UserRepository userRepository;

	@Mock
	private CartRepository cartRepository;

	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Before
	public void setup() throws NoSuchFieldException {
		this.userController = new UserController();

		TestUtils.mockField(userController, "userRepository", this.userRepository);
		TestUtils.mockField(userController, "cartRepository", this.cartRepository);
		TestUtils.mockField(userController, "bCryptPasswordEncoder", this.bCryptPasswordEncoder);
	}

	@Test
	public void should_find_user_by_id() {
		User userMock = new User();

		userMock.setUsername("foo");

		Mockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(userMock));

		ResponseEntity<User> responseEntity = this.userController.findById(1L);

		Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}

	@Test
	public void should_find_user_by_name() {
		String usernameMock = "foobar";
		User userMock = new User();

		Mockito.when(this.userRepository.findByUsername(usernameMock)).thenReturn(userMock);

		ResponseEntity<User> responseEntity = this.userController.findByUserName(usernameMock);

		Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}

	@Test
	public void should_create_new_user() {
		CreateUserRequest userRequest = new CreateUserRequest();

		userRequest.setUsername("foo");
		userRequest.setPassword("12345678");
		userRequest.setConfirmPassword("12345678");

		Mockito.when(this.cartRepository.save(ArgumentMatchers.any())).thenReturn(null);
		Mockito.when(this.userRepository.save(ArgumentMatchers.any())).thenReturn(null);
		Mockito.when(this.bCryptPasswordEncoder.encode(ArgumentMatchers.any())).thenReturn("hashed-password");

		ResponseEntity<User> responseEntity = this.userController.createUser(userRequest);

		Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}

	@Test
	public void should_return_NOTFOUND_when_finding_wrong_user_name() {
		Mockito.when(this.userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(null);

		ResponseEntity<User> responseEntity = this.userController.findByUserName("foo");

		Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}

	@Test
	public void should_return_BADREQUEST_when_creating_invalid_iser() {
		CreateUserRequest userRequest = new CreateUserRequest();

		userRequest.setPassword("123");

		ResponseEntity<User> responseEntity = this.userController.createUser(userRequest);

		Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}
}
