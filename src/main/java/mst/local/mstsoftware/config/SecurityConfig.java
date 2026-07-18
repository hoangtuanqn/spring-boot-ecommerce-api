package mst.local.mstsoftware.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.AllArgsConstructor;
import mst.local.mstsoftware.filters.JwtAuthFilter;
import mst.local.mstsoftware.filters.TraceIdFilter;

@AllArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final TraceIdFilter traceIdFilter;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // 1. Route AUTH - No JWT
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/refresh").permitAll()
                        // 2. Public API
                        .requestMatchers("/api/v1/products").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // UsernamePasswordAuthenticationFilter.class chỉ làm mốc để tham chiếu
                // sau khi chạy qua jwtAuthFiler thì nó sẽ chạy qua bên
                // UsernamePasswordAuthenticationFilter.class (chạy nma ko làm gì)
                // cần phải có 2 tham số
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(traceIdFilter, JwtAuthFilter.class);

        return http.build();
    }

}
