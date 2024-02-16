package com.jwt.authentication.util;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import org.apache.commons.lang3.time.DateUtils;

public class JwtUtils {

	private static final SecretKey secretKey = SIG.HS256.key()
			.build();

	private static final String ISSUER = "DEMO_ISSUER";

	private JwtUtils() {
	}

	public static boolean validateToken(String jwtToken) {

		Optional<Claims> claimsOptional = parseToken(jwtToken);
		return claimsOptional.isPresent();

	}

	private static Optional<Claims> parseToken(String jwtToken) {

		try {
			JwtParser jwtParser = Jwts.parser()
					.verifyWith(secretKey)
					.build();

			return Optional.of(jwtParser.parseSignedClaims(jwtToken)
					.getPayload());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Optional.empty();

	}

	public static Optional<String> getUsernameFromToken(String jwtToken) {

		Optional<Claims> claimsOptional = parseToken(jwtToken);

		return claimsOptional.map(Claims::getSubject);
	}

	public static String generateToken(String username) {

		Date current = new Date();
		int jwtExpirationInMinutes = 10;
		return Jwts.builder()
				.id(UUID.randomUUID()
						.toString())
				.issuer(ISSUER)
				.subject(username)
				.signWith(secretKey)
				.issuedAt(current)
				.expiration(DateUtils.addMinutes(current, jwtExpirationInMinutes))
				.compact();

	}
}
