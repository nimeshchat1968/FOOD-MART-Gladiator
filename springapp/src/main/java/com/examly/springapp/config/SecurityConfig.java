package com.examly.springapp.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
@Autowired
JwtAuthenticationEntryPoint entryPoint;
@Autowired
UserDetailsService userDetailsService;
@Autowired
PasswordEncoder encoder;
@Autowired
JwtAuthenticationFilter jwtAuthenticationFilter;
@Autowired
public void configure(AuthenticationManagerBuilder authority)throws Exception{
    authority.userDetailsService(userDetailsService).passwordEncoder(encoder);
}
@Bean
public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception{
    return http.getSharedObject(AuthenticationManagerBuilder.class)
    .userDetailsService(userDetailsService)
    .passwordEncoder(encoder)
    .and()
    .build();
}
@Bean
public SecurityFilterChain cFilterChain(HttpSecurity http)throws Exception{
     http.cors(cors->cors.disable())
    .csrf(csrf->csrf.disable())
    .authorizeHttpRequests(auth->auth
    .requestMatchers("/api/register","/api/login").permitAll()
    .requestMatchers(HttpMethod.POST, "/api/food").hasRole("ADMIN")
    .requestMatchers(HttpMethod.GET,"/api/food").hasAnyRole("ADMIN","USER")
    .requestMatchers(HttpMethod.PUT, "/api/food/{foodId}").hasRole("ADMIN")
    .requestMatchers(HttpMethod.DELETE, "/api/food/{foodId}").hasRole("ADMIN")

    .requestMatchers(HttpMethod.POST, "/api/orders","/api/orders/s").hasRole("USER")
    .requestMatchers(HttpMethod.GET, "/api/orders").hasRole("ADMIN")
    .requestMatchers(HttpMethod.GET, "/api/orders/{orderId}").permitAll()
    .requestMatchers(HttpMethod.GET, "/api/orders/user/{userId}").hasRole("USER")
    .requestMatchers(HttpMethod.PUT, "/api/orders/{orderId}").hasRole("ADMIN")
    .requestMatchers(HttpMethod.DELETE, "/api/orders/{orderId}").permitAll()

    .requestMatchers(HttpMethod.POST, "/api/feedback").hasRole("USER")
    .requestMatchers(HttpMethod.GET,"/api/feedback").hasRole("ADMIN")
    .requestMatchers(HttpMethod.GET,"/api/feedback/{id}").hasRole("ADMIN")
    .requestMatchers(HttpMethod.GET, "/api/feedback/user/{userId}").hasRole("USER")
    .requestMatchers(HttpMethod.DELETE, "/api/feedback/{id}").permitAll()
    .requestMatchers("/swagger-ui/**","/v3/api-docs/**","/swagger-ui.html").permitAll()
    .anyRequest().permitAll())
    .exceptionHandling(exception->exception.authenticationEntryPoint(entryPoint))
    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    
    return http.build();
}
}
