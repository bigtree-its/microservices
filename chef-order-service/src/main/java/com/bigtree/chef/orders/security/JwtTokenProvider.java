package com.bigtree.chef.orders.security;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.bigtree.chef.orders.error.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Component
public class JwtTokenProvider {

    @Value("${security.jwt.token.security-key:secret}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds;

    public String resolveToken(final HttpServletRequest request) {
        final String bearerToken = request.getHeader("Authorization");
        if (StringUtils.isNotEmpty(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(final String token) {
        try {
            final Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            if ( claims.getBody().getExpiration().before(new Date())){
                return false;
            }
        }catch ( JwtException | IllegalArgumentException | ArrayIndexOutOfBoundsException e){
            throw new ApiException(HttpStatus.UNAUTHORIZED, "JWT is invalid or expired");
        }
        return true;
    }

	public Authentication getAuthentication(String token) {
		return null;
	}
    
}
