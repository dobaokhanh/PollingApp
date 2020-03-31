package com.poll.payload;

import javax.validation.constraints.NotBlank;

public class LoginRequest {

	@NotBlank
	private String userOrEmail;

	@NotBlank
	private String password;

	public String getUserOrEmail() {
		return userOrEmail;
	}

	public void setUserOrEmail(String userOrEmail) {
		this.userOrEmail = userOrEmail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
