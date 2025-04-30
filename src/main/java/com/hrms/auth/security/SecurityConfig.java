// src/main/java/com/hrms/auth/security/SecurityConfig.java
package com.hrms.auth.security;

import com.hrms.auth.service.AuthService;              // only for AuthenticationProvider
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    /** register the filter as its own bean – no field injection */
   // @Bean
    //public JwtAuthenticationFilter jwtAuthenticationFilter() {
     //   return new JwtAuthenticationFilter();
   // }
	
	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter(
	        JwtUtil jwtUtil,            // injected automatically
	        AuthService authService) {   // injected automatically
	    return new JwtAuthenticationFilter(jwtUtil, authService);
	}


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           JwtAuthenticationFilter filter) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/auth/login").permitAll()
                    .anyRequest().authenticated())
            .sessionManagement(sess -> sess.sessionCreationPolicy(
                    org.springframework.security.config.http.SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider(null, null)) // placeholder, see lower bean
            .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /** AuthenticationProvider now built via constructor injection */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(AuthService authService,
                                                            PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(authService);
        provider.setPasswordEncoder(encoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg)
            throws Exception {
        return cfg.getAuthenticationManager();
    }
}
