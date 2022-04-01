package com.bigtree.chef.orders.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtTokenFilter extends GenericFilterBean {

    private JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

	@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.info("doFilter beings..");
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        log.info("Token from request {}", token);
        try {
            if (StringUtils.isNotEmpty(token) && jwtTokenProvider.validateToken(token)) {
                log.info("Token validated..");
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                log.info("Authentication object derived from token {}", authentication);
                log.info("The principal obejct {}", authentication.getPrincipal());
                UsernamePasswordAuthenticationToken authenticated = new UsernamePasswordAuthenticationToken(
                        authentication.getPrincipal(), authentication.getCredentials(),
                        authentication.getAuthorities());
                log.info("Set the Authenticated obejct to  SecurityContextHolder {}", authenticated);
                SecurityContextHolder.getContext().setAuthentication(authenticated);
            }
        } catch (Exception e) {
            //Already logged
        }
        chain.doFilter(request, response);
    }
    
}
