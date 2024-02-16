package com.jwt.authentication.service.auth.impl;

import java.util.List;

import com.jwt.authentication.entity.AppUser;
import com.jwt.authentication.repos.AppUserRepo;
import com.jwt.authentication.service.auth.IAuthService;
import com.jwt.authentication.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;
	private final AppUserRepo appUserRepo;

	@Override
	public String login(String username, String password) {
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
				new UsernamePasswordAuthenticationToken(
				username, password);

		Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

		// generate token
		return JwtUtils.generateToken(((UserDetails) (authenticate.getPrincipal())).getUsername());

	}

	@Override
	public String signup(String name, String username, String password) {

		if (appUserRepo.existsByUserName(username)) {
			throw new RuntimeException("User already exists.");
		}

		//encode password

		String encodePassword = passwordEncoder.encode(password);

		AppUser appUser = AppUser.builder()
				.name(name)
				.userName(username)
				.password(encodePassword)
				.authorities(List.of(new SimpleGrantedAuthority("ROLE_USER")))
				.build();

		//save entity
		appUserRepo.save(appUser);

		//JET utils to generate the token
		return JwtUtils.generateToken(username);

	}
}
