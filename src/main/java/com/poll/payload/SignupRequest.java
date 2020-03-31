package com.poll.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SignupRequest {
	
	@NotBlank
	@Size(max = 40, min = 4)
	private String name;
	
	@NotBlank
	@Size(min = 3, max = 15)
	private String username;
	
	@NotBlank
	@Size(max = 40, min = 5)
	private String email;
	
	@NotBlank
	@Size(max = 25, min = 6)
	private String password;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
