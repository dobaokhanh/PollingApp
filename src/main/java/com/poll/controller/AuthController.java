package com.poll.controller;

import java.net.URI;
import java.util.Collections;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poll.entity.Role;
import com.poll.entity.RoleName;
import com.poll.entity.User;
import com.poll.exception.AppException;
import com.poll.payload.ApiResponse;
import com.poll.payload.JwtAuthenticationResponse;
import com.poll.payload.LoginRequest;
import com.poll.payload.SignupRequest;
import com.poll.repository.RoleRepository;
import com.poll.repository.UserRepository;
import com.poll.security.JwtTokenProvider;

public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtTokenProvider jwtTokenProvider;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUserOrEmail(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = jwtTokenProvider.generateToken(authentication);
		return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
		if (userRepository.existsByUsername(signupRequest.getUsername())) {
			return new ResponseEntity(new ApiResponse(false, "Username is already taken !"), HttpStatus.BAD_REQUEST);
		}

		if (userRepository.existsByEmail(signupRequest.getEmail())) {
			return new ResponseEntity(new ApiResponse(false, "Email is already taken !"), HttpStatus.BAD_REQUEST);
		}

		User user = new User(signupRequest.getName(), signupRequest.getUsername(), signupRequest.getEmail(),
				signupRequest.getPassword());

		user.setPassword(passwordEncoder.encode(user.getPassword()));

		Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
				.orElseThrow(() -> new AppException("User Role is not set!"));
		
		user.setRoles(Collections.singleton(userRole));
		
		User result = userRepository.save(user);
		
		URI location = ServletUriComponentsBuilder
							.fromCurrentContextPath().path("/api/users/{username}")
							.buildAndExpand(result.getUsername()).toUri();
		return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
	}
}
