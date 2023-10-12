package com.mvc.project.security.service;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class JwtService {

	public String extractUserMail(String token) {
		return extractClaims(token).getIssuer();
	}
	
	public Date extractExpirationTime(String token) {
		return extractClaims(token).getExpiration();
	}
	
	private Claims extractClaims(String token) {
		return Jwts.parser()
					.verifyWith(generateKey())
					.build()
					.parseSignedClaims(token)
					.getPayload();
	}
	
	private SecretKey generateKey() {
		SecretKey key = null;
		try {
			key = KeyGenerator.getInstance("HmacSHA256").generateKey();
		} catch (NoSuchAlgorithmException e) {			
			e.printStackTrace();
		}
		return key;
	}
	
	public String generateToken(String username) {
		return createToken(username, new HashMap<String, Object>());
	}
	
	public String generateToken(String username, Map<String, Object> claim) {
		return createToken(username, claim);
	}
	
	private String createToken(String username, Map<String, Object> claim) {
		return Jwts.builder()
					.subject(username)
					.issuedAt(new Date(System.currentTimeMillis()))
					.expiration(new Date(System.currentTimeMillis() + 1000 * 30 * 10))
					.claims(claim)
					.signWith(generateKey())
					.compact();
	}
	
	public Boolean validateToken(String token) {
		return (extractExpirationTime(token).before(new Date()));
	}
}
