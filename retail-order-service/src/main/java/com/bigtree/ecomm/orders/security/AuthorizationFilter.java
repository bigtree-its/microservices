package com.bigtree.ecomm.orders.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
                log.info("Validating request..");
                if ( validateRequest(request)){
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("customer", null, new ArrayList<>());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    log.info("Authorized");
                    filterChain.doFilter(request, response);
                }else{
                    log.info("UnAuthorized");
                    filterChain.doFilter(request, response);
                }
        
	}

    private boolean validateRequest(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if ( StringUtils.isNotEmpty(authorization) && authorization.startsWith("Bearer")){
            return true;
        }
        return false;
    }
    
}
