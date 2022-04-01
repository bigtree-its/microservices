package com.bigtree.chef.orders.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //@formatter:off
        http
        .httpBasic().disable()
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers("/orders").permitAll();
        // .antMatchers(HttpMethod.GET, "/orders/**").permitAll()
        // .anyRequest().authenticated()   
        // .filterSecurityInterceptorOncePerRequest(true)
        // .and()
        // .addFilterBefore(new AuthorizationFilter(), BasicAuthenticationFilter.class);
        //@formatter:on
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // web.ignoring().antMatchers("/orders/**", "/auth/signin", "/home");
        super.configure(web);
    }

}

