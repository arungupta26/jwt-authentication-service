package com.jwt.authentication.service.userdetails;

import com.jwt.authentication.entity.AppUser;
import com.jwt.authentication.repos.AppUserRepo;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final AppUserRepo appUserRepo;

	public CustomUserDetailsService(AppUserRepo appUserRepo) {
		this.appUserRepo = appUserRepo;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser appUser = appUserRepo.findByUserName(username)
				.orElseThrow(() -> new UsernameNotFoundException("User: " + username + " not " + "found"));

		return new User(appUser.getUserName(), appUser.getPassword(), appUser.getAuthorities());
	}
}
