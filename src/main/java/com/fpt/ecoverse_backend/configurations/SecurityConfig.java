package com.fpt.ecoverse_backend.configurations;

import com.fpt.ecoverse_backend.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableWebMvc
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/login",
            "/api/auth/student/login",
            "/api/auth/refresh",
            "/api/auth/logout",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api-docs",
            "/api-docs.yaml",
            "/webjars/**",
            "/",
            "/partnerships/register"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for stateless JWT authentication
                .csrf(AbstractHttpConfigurer::disable)

                // Enable CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Set session management to stateless
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Set permissions on endpoints
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()

                        // Admin endpoints
                        .requestMatchers("/admins/**").hasRole("ADMIN")

                        // Partnership endpoints
                        .requestMatchers(HttpMethod.GET, "/partnerships/{partnership_id}").hasAnyRole("PARTNERSHIP", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/partnerships/{partnership_id}/profile").hasRole("PARTNERSHIP")
                        .requestMatchers(HttpMethod.POST, "/partnerships/{partnership_id}/bulk-create").hasRole("PARTNERSHIP")
                        .requestMatchers(HttpMethod.GET, "/partnerships//{partnership_id}/students/{student_id}").hasAnyRole("PARTNERSHIP", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/partnerships/{partnership_id}/students/{student_id}").hasRole("PARTNERSHIP")
                        .requestMatchers(HttpMethod.GET, "/partnerships/{partnership_id}/accounts").hasAnyRole("PARTNERSHIP", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/partnerships/{partnership_id}/students").hasRole("PARTNERSHIP")
                        .requestMatchers(HttpMethod.DELETE, "/partnerships/{partnership_id}").hasAnyRole("PARTNERSHIP", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/partnerships/{partnership_id}/parents").hasRole("PARTNERSHIP")

                        // Parent endpoints
                        .requestMatchers(HttpMethod.POST, "/parents/{parent_id}/students/{student_id}/link-student").hasRole("PARENT")
                        .requestMatchers(HttpMethod.GET, "/parents/{parent_id}/students").hasRole("PARENT")
                        .requestMatchers(HttpMethod.PUT, "/parents/{parent_id}").hasRole("PARENT")
                        .requestMatchers(HttpMethod.GET, "/parents/{parent_id}").hasRole("PARENT")
                        .requestMatchers(HttpMethod.DELETE, "/parents/{parent_id}").hasAnyRole("PARENT", "PARTNERSHIP")

                        // Student endpoints
                        .requestMatchers("/students/**").hasAnyRole("STUDENT", "PARENT", "ADMIN", "PARTNERSHIP")

                        // Waste endpoints
                        .requestMatchers(HttpMethod.GET, "/wastes/bins").hasAnyRole("STUDENT", "PARTNERSHIP", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/wastes/{user_id}").hasAnyRole("PARTNERSHIP", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "wastes/bins/{admin_id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/wastes/items/users/{user_id}/game-rounds/{game_round_id}").hasAnyRole("STUDENT", "PARTNERSHIP", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/wastes/items/{waste_item_id}/users/{user_id}").hasAnyRole("PARTNERSHIP", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/wastes/bins/{waste_bin_id}/admins/{admin_id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/wastes/items/{waste_item_id}/users/{user_id}").hasAnyRole("PARTNERSHIP", "ADMIN")

                        // Game endpoints
                        .requestMatchers(HttpMethod.POST, "/games/rounds/users/{user_id}").hasAnyRole("PARTNERSHIP", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/games/rounds/users/{user_id}").hasAnyRole("STUDENT", "PARTNERSHIP", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/games/rounds/{game_round_id}/users/{user_id}").hasAnyRole("PARTNERSHIP", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/games/rounds/{game_round_id}/users/{user_id}").hasAnyRole("PARTNERSHIP", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/games/rounds/{game_round_id}/students/{student_id}/attempts").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.GET, "/games/rounds/{game_round_id}/students/{student_id}/attempts").hasAnyRole("STUDENT", "PARTNERSHIP", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/games/attempts/{game_attempt_id}").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.POST, "/games/rounds/{game_round_id}/attempts/{game_attempt_id}/placements").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.GET, "/games/attempts/{game_attempt_id}/placements").hasRole("STUDENT")

                        // Reward endpoints
                        .requestMatchers(HttpMethod.POST, "/rewards/{partner_id}").hasRole("PARTNERSHIP")
                        .requestMatchers(HttpMethod.GET, "/rewards/{partner_id}").hasAnyRole("STUDENT", "PARTNERSHIP")
                        .requestMatchers(HttpMethod.GET, "/rewards/items/{reward_item_id}/partners/{partner_id}").hasAnyRole("STUDENT", "PARTNERSHIP")
                        .requestMatchers(HttpMethod.PUT, "/rewards/items/{reward_item_id}/partners/{partner_id}").hasRole("PARTNERSHIP")
                        .requestMatchers(HttpMethod.DELETE, "/rewards/items/{reward_item_id}/partners/{partner_id}").hasRole("PARTNERSHIP")

                        // All other requests need authentication
                        .anyRequest().authenticated()
                )

                // Add JWT filter before UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
