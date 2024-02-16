package com.jwt.authentication.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

@Document(collection = "app-user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {

	@Id
	private String id;
	private String name;
	private String userName;
	private String password;
	private List<GrantedAuthority> authorities;
}
