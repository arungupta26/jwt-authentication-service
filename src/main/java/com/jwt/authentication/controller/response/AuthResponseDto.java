package com.jwt.authentication.controller.response;

import com.jwt.authentication.enums.AuthStatus;

public record AuthResponseDto(String token, AuthStatus authStatus) {}
