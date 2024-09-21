package com.app.cv.config;

import com.app.cv.service.AdminService;
import com.app.cv.service.OwnerService;
import com.app.cv.service.util.JwtUtil;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AdminService adminService;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/cv-auth/auth/login", "/cv-auth/admin/register", "/cv-auth/owner/login").permitAll()  // Allow Owner login as well
                    .anyRequest().authenticated()
            )
            .addFilterBefore(new JwtRequestFilter(jwtUtil, adminService, ownerService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        DaoAuthenticationProvider adminAuthProvider = new DaoAuthenticationProvider();
        adminAuthProvider.setUserDetailsService(adminService);
        adminAuthProvider.setPasswordEncoder(passwordEncoder());

        DaoAuthenticationProvider ownerAuthProvider = new DaoAuthenticationProvider();
        ownerAuthProvider.setUserDetailsService(ownerService);
        ownerAuthProvider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(List.of(adminAuthProvider, ownerAuthProvider));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter(jwtUtil, adminService, ownerService);
    }
}
