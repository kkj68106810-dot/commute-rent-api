package com.commute.api.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC関連の設定クラス。
 * Configuration class for Web MVC settings.
 *
 * <p>
 * CORS（Cross-Origin Resource Sharing）の許可設定を定義する。
 * Defines CORS (Cross-Origin Resource Sharing) allow rules.
 * </p>
 *
 * @author Kim Gwangjin
 * @since 2026/09/27
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * CORSマッピングを追加する。
     * Adds CORS mappings.
     *
     * @param registry CORSレジストリ / CORS registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // CORS適用対象のAPIパス / API path pattern for CORS
                .allowedOrigins("http://localhost:3000") // Next.jsフロントエンドのオリジン / Next.js frontend origin
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true) // Cookie・認証ヘッダを許可 / Allow cookies and auth headers
                .maxAge(3600); // Preflight結果のキャッシュ時間（秒） / Preflight cache duration (seconds)
    }
}
