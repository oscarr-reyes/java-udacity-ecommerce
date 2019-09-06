package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		log.info("Find by id method invoked: {}", id);

		return ResponseEntity.of(userRepository.findById(id));
	}

	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		log.info("Find by user name method invoked: {}", username);

		User user = userRepository.findByUsername(username);

		if (user == null) {
			log.error("Unable to find user by name: {}", username);

			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		log.info("Find by user name method success");

		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		log.info("Create user method invoked");

		if (createUserRequest.getPassword().length() < 7 || !createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
			log.error("User create request invalid, password incorrect");

			return ResponseEntity.badRequest().build();
		}

		User user = new User();

		user.setUsername(createUserRequest.getUsername());
		user.setPassword(this.bCryptPasswordEncoder.encode(createUserRequest.getPassword()));

		Cart cart = new Cart();

		cartRepository.save(cart);

		user.setCart(cart);

		userRepository.save(user);

		user.setPassword(null);

		log.info("Create user method success");

		return ResponseEntity.ok(user);
	}

}
