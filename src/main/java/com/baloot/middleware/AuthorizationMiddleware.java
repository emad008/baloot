package com.baloot.middleware;

import com.baloot.model.User;
import com.baloot.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;

public class AuthorizationMiddleware {
	private final UserRepository userRepository;
	private String lastAuthorizedUserName;

	public AuthorizationMiddleware(UserRepository userRepository) {
		this.userRepository = userRepository;
		this.lastAuthorizedUserName = null;
	}

	public void setLastAuthorizedUser(String username) {
		this.userRepository.findByUsername(username);
		this.lastAuthorizedUserName = username;
	}

	public void removeAuthorizedUser() {
		this.lastAuthorizedUserName = null;
	}

	public void authorize(HttpServletRequest request) {
		if (this.lastAuthorizedUserName == null) {
			request.setAttribute("user", null);
			return;
		}
		request.setAttribute("user", this.userRepository.findByUsername(this.lastAuthorizedUserName));
	}
}
