package com.commute.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 通勤家賃APIアプリケーションのエントリーポイント。
 * Entry point of the Commute Yaching (rent) API application.
 *
 * <p>
 * Spring Bootアプリケーションを起動し、各ドメインのBeanを初期化する。
 * Starts the Spring Boot application and initializes beans across domains.
 * </p>
 *
 * @author Kim Gwangjin
 * @since 2026/09/27
 */
@SpringBootApplication
public class CommuteRentApiApplication {

    /**
     * アプリケーションのメインメソッド。
     * Main method of the application.
     *
     * @param args コマンドライン引数 / Command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(CommuteRentApiApplication.class, args);
    }

}
