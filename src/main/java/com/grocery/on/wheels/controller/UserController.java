package com.grocery.on.wheels.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grocery.on.wheels.dao.UserRepository;
import com.grocery.on.wheels.model.User;

@RestController
@RequestMapping("/grocery/user")
public class UserController {
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/users")
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
	
	@PostMapping("/login")
	public ResponseEntity<User> login(@RequestBody User userDetail) {
		User user = userRepository.login(userDetail);
		return ResponseEntity.ok(user);
	}
	
	@PostMapping("/acitvate")
	public User acitvate(@RequestBody User userDetail) {
		userRepository.acitvate(userDetail);
		return userRepository.findByUsername(userDetail.getUsername());
	}
	

	@PostMapping("/users")
	public User createUser(@RequestBody User user) {
		if (userRepository.findByUsername(user.getUsername()) == null) {
			userRepository.insert(user);
			return userRepository.findByUsername(user.getUsername());
		}
		return null;
	}

	@GetMapping("/users/{username}")
	public ResponseEntity<User> getUserByUsername(@PathVariable("username") String username) {
		User user = userRepository.findByUsername(username);
		return ResponseEntity.ok(user);
	}

	@PutMapping("/users/{username}")
	public ResponseEntity<User> updateUser(@PathVariable("username") String pathusername, @RequestBody User userDetails) {
		userRepository.update(pathusername, userDetails);
		return ResponseEntity.ok(userRepository.findByUsername(pathusername));
	}

	@DeleteMapping("/users/{username}")
	public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable("username") String username) {
		userRepository.deleteByUsername(username);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}
}