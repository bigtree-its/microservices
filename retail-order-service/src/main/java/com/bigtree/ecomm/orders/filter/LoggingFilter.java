package com.bigtree.ecomm.orders.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Order(1)
@Slf4j
public class LoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        ThreadContext.put("correlation-id", req.getHeader("incoming-correlation-id"));
        Enumeration<String> headerNames = req.getHeaderNames();
        log.info("Request Headers");
        while(headerNames.hasMoreElements()){
            String nextElement = headerNames.nextElement();
            log.info("Header {}:{}", nextElement, req.getHeader(nextElement));
        }
        chain.doFilter(request, response);
    }

}