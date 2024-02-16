package com.jwt.authentication.controller;

import com.jwt.authentication.controller.request.AuthRequestDto;
import com.jwt.authentication.controller.response.AuthResponseDto;
import com.jwt.authentication.enums.AuthStatus;
import com.jwt.authentication.service.auth.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

	private final IAuthService authService;

	@PostMapping("/login")
	public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto authRequestDto) {

		String jwtToken = authService.login(authRequestDto.username(), authRequestDto.password());

		return ResponseEntity.ok(new AuthResponseDto(jwtToken, AuthStatus.LOGIN_SUCCESS));

	}

	@PostMapping("/signup")
	public ResponseEntity<AuthResponseDto> signUp(@RequestBody AuthRequestDto authRequestDto) {

		try {
			String jwtToken = authService.signup(authRequestDto.name(), authRequestDto.username(),
					authRequestDto.password());

			return ResponseEntity.ok(new AuthResponseDto(jwtToken, AuthStatus.USER_CREATED_SUCCESSFULLY));
		} catch (Exception e) {

			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new AuthResponseDto(null, AuthStatus.USER_NOT_CREATED));
		}
	}

}
