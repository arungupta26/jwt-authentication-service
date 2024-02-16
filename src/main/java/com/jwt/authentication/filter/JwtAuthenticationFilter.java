package com.jwt.authentication.filter;

import java.io.IOException;
import java.util.Optional;

import com.jwt.authentication.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		//fetch token from request
		Optional<String> jwtTokenOptionl = getTokenFromRequest(request);

		//validate jwt token
		if (jwtTokenOptionl.isPresent()) {
			String jwtToken = jwtTokenOptionl.get();
			if (JwtUtils.validateToken(jwtToken)) {
				//get username from jwt token
				Optional<String> userNameOptional = JwtUtils.getUsernameFromToken(jwtToken);

				userNameOptional.ifPresent(username -> {

					//fetch user details with the help username
					UserDetails userDetails = userDetailsService.loadUserByUsername(userNameOptional.get());

					//create Authentication token
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
							new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());

					usernamePasswordAuthenticationToken.setDetails(
							new WebAuthenticationDetailsSource().buildDetails(request));

					//set authentication token to security context

					SecurityContextHolder.getContext()
							.setAuthentication(usernamePasswordAuthenticationToken);

				});
			}
		} else {
			logger.warn("JWT token not generated");
		}

		//pass request and response to next filter
		filterChain.doFilter(request, response);

	}

	private Optional<String> getTokenFromRequest(HttpServletRequest request) {

		//extract authentication header
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		//Bearer <JWT TOKEN>
		if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer "))
			return Optional.of(authHeader.substring(7));
		else {
			logger.error("Auth not present in Headers");
			return Optional.empty();
		}
	}
}
