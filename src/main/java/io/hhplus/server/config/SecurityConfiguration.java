package io.hhplus.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                // 특정 API에 대해 모든 사용자에게 접근 허용
                .requestMatchers("/concerts/**").permitAll()
                .requestMatchers("/reservations/**").permitAll()
                .requestMatchers("/users/**").permitAll()
                .requestMatchers("/payments/**").permitAll()
                // --------------------------------------------
                .anyRequest().authenticated(); // 나머지 API에 대해서는 인증을 요구
        http
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
