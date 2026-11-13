package com.commute.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Spring Securityの設定クラス。
 * Configuration class for Spring Security.
 *
 * <p>
 * REST API向けにCSRFを無効化し、CORSおよび認可ルールを定義する。
 * Disables CSRF for REST APIs and defines CORS and authorization rules.
 * </p>
 *
 * @author Kim Gwangjin
 * @since 2026/09/27
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * セキュリティフィルタチェーンを構築する。
     * Builds the security filter chain.
     *
     * @param http HttpSecurity設定オブジェクト / HttpSecurity configuration object
     * @return 構築したSecurityFilterChain / Built SecurityFilterChain
     * @throws Exception 設定構築時の例外 / Exception thrown during configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable) // REST APIでは通常無効化 / Usually disabled for REST APIs
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").permitAll()
                        .anyRequest().authenticated()
                ).oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/", true))
        ;

        return http.build();
    }

    /**
     * CORS設定ソースを生成する。
     * Creates the CORS configuration source.
     *
     * @return CORS設定ソース / CORS configuration source
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // Next.jsサーバアドレス / Next.js server address
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
