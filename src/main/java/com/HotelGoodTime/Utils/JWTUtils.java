package com.HotelGoodTime.Utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class JWTUtils {
	
	private static final long EXPIRATION_TIME = 1000 * 60 * 24 * 1; //for 1 day
	
	private final SecretKey Key;
	
	public JWTUtils() {
		
		String secreteString = "556221445889966214568222DDE1155DD1D52226669974CC52235C48D5D2C448996D3D114FF8F2226C24D78F8522X11C1V455D52244D22001S55D5C22X2V233156DD55";
		byte[] keyBytes = Base64.getDecoder().decode(secreteString.getBytes(StandardCharsets.UTF_8));
		this.Key = new SecretKeySpec(keyBytes, "HmacSHA256");
	}
	
	public String genrateToken(UserDetails userDetails) {
		
		return Jwts.builder()
				.subject(userDetails.getUsername())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(Key)
				.compact();
	}
	
	public String extractUsername(String token) {
		return extractClaims(token, Claims::getSubject);
	}

	private <T> T extractClaims(String token, Function<Claims, T> claimFunction) {
		
		return claimFunction.apply(Jwts.parser().verifyWith(Key).build().parseSignedClaims(token).getPayload());
	}
	
	public boolean isValidToken(String token , UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
		
		return extractClaims(token, Claims::getExpiration).before(new Date());
	}

}
