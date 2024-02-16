package com.jwt.authentication.service.auth;

import org.springframework.stereotype.Service;

@Service
public interface IAuthService {
	String login(String username, String password);

	String signup(String name, String username, String password);

}
