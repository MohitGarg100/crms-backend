package com.crms.security;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


@Service
public class JwtService {
	
	@Value("${jwt.secret}")
	private String SECRET_KEY;
	
	private static final long JWT_EXPIRATION = 1000*60*90;
	
	public String generateToken(String username) {
		
		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
				.signWith(getSignKey(), SignatureAlgorithm.HS256)
				.compact();
	}
	
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public String extractRole(String token) {
		return extractAllClaims(token).get("role", String.class);
	}
	
	public boolean isTokenValid(String token, String username) {
		final String extractedUsername = extractUsername(token);
		return extractedUsername.equals(username) && !isTokenExpired(token);
	}
	
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSignKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	private Key getSignKey() {
		return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
	}

}
