-- =====================================================================
-- CS-DB-001  テーブル定義書  DDL  v1.0  (2026-10-10)
-- 東京 家賃・通勤シミュレーター (Commute Simulator Tokyo)
-- DBMS: MariaDB 12.3.2 / InnoDB / utf8mb4 (utf8mb4_uca1400_ai_ci)
-- 作成者: Kim GwangJin
-- 根拠: DB設計書 最終版（テーブル定義・物理モデル）。DB名 commute_sim と FK削除規則は前提
-- =====================================================================

CREATE DATABASE IF NOT EXISTS commute_rent
    DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci;
USE commute_rent;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS favorite_stations, users, commute_times, rent_stats, line_stations, rail_lines, stations, prefectures;
SET FOREIGN_KEY_CHECKS = 1;

-- prefectures : 都道府県
CREATE TABLE prefectures (
                             prefecture_id      BIGINT         NOT NULL AUTO_INCREMENT COMMENT '都道府県ID',
                             prefecture_name    VARCHAR(20)    NOT NULL COMMENT '都道府県名',
                             name_kana          VARCHAR(100)    NULL COMMENT '都道府県名（カタカナ）',
                             name_en            VARCHAR(100)    NULL COMMENT '都道府県名（ローマ字）',
                             CONSTRAINT pk_prefectures PRIMARY KEY (prefecture_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci COMMENT='都道府県';

-- stations : 駅
CREATE TABLE stations (
                          station_id         BIGINT         NOT NULL AUTO_INCREMENT COMMENT '駅ID',
                          prefecture_id      BIGINT         NOT NULL COMMENT '都道府県ID',
                          station_name       VARCHAR(20)    NOT NULL COMMENT '駅名',
                          name_kana          VARCHAR(100)    NULL COMMENT '駅名（カタカナ）',
                          name_en            VARCHAR(100)    NULL COMMENT '駅名（ローマ字）',
                          latitude           DECIMAL(10,8)  NOT NULL COMMENT '緯度（y）',
                          longitude          DECIMAL(11,8)  NOT NULL COMMENT '経度（x）',
                          CONSTRAINT pk_stations PRIMARY KEY (station_id),
                          KEY idx_stations_lat_lng (latitude, longitude),
                          KEY idx_stations_prefecture (prefecture_id),
                          CONSTRAINT fk_stations_prefecture FOREIGN KEY (prefecture_id) REFERENCES prefectures(prefecture_id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci COMMENT='駅';

-- rail_lines : 路線
CREATE TABLE rail_lines (
                            line_id            BIGINT         NOT NULL AUTO_INCREMENT COMMENT '路線ID',
                            line_name          VARCHAR(20)    NOT NULL COMMENT '路線名',
                            name_kana          VARCHAR(100)    NULL COMMENT '路線名（カタカナ）',
                            name_en            VARCHAR(100)    NULL COMMENT '路線名（ローマ字）',
                            CONSTRAINT pk_rail_lines PRIMARY KEY (line_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci COMMENT='路線';

-- line_stations : 路線_駅マッピング
CREATE TABLE line_stations (
                               line_station_id    BIGINT         NOT NULL AUTO_INCREMENT COMMENT '路線_駅ID',
                               line_id            BIGINT         NOT NULL COMMENT '路線ID',
                               station_id         BIGINT         NOT NULL COMMENT '駅ID',
                               `sequence`         INT            NOT NULL COMMENT '並び順',
                               CONSTRAINT pk_line_stations PRIMARY KEY (line_station_id),
                               KEY idx_ls_line_station (line_id, station_id),
                               KEY idx_ls_station (station_id),
                               CONSTRAINT fk_ls_line FOREIGN KEY (line_id) REFERENCES rail_lines(line_id) ON DELETE CASCADE ON UPDATE CASCADE,
                               CONSTRAINT fk_ls_station FOREIGN KEY (station_id) REFERENCES stations(station_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci COMMENT='路線_駅マッピング';

-- rent_stats : 駅別家賃
CREATE TABLE rent_stats (
                            rent_stat_id       BIGINT         NOT NULL AUTO_INCREMENT COMMENT '統計ID',
                            station_id         BIGINT         NOT NULL COMMENT '駅ID',
                            room_layout        VARCHAR(10)    NOT NULL COMMENT '間取り',
                            average_rent       INT            NOT NULL COMMENT '平均家賃',
                            min_rent           INT            NULL COMMENT '最小家賃',
                            max_rent           INT            NULL COMMENT '最大家賃',
                            updated_at         TIMESTAMP      NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
                            CONSTRAINT pk_rent_stats PRIMARY KEY (rent_stat_id),
                            CONSTRAINT uq_rent_station_layout UNIQUE (station_id, room_layout),
                            KEY idx_rent_layout_avg (room_layout, average_rent),
                            CONSTRAINT chk_rent_range CHECK (min_rent <= average_rent AND average_rent <= max_rent),
                            CONSTRAINT fk_rent_station FOREIGN KEY (station_id) REFERENCES stations(station_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci COMMENT='駅別家賃';

-- commute_times : 通勤時間
CREATE TABLE commute_times (
                               dest_station_id    BIGINT         NOT NULL COMMENT '到着駅ID（勤務地）',
                               origin_station_id  BIGINT         NOT NULL COMMENT '出発駅ID',
                               minutes            SMALLINT       NOT NULL COMMENT '所要時間（分）',
                               transfers          TINYINT        NOT NULL COMMENT '乗換回数',
                               CONSTRAINT pk_commute_times PRIMARY KEY (dest_station_id, origin_station_id),
                               KEY idx_commute_dest_min (dest_station_id, minutes),
                               KEY idx_commute_origin (origin_station_id),
                               CONSTRAINT fk_commute_dest FOREIGN KEY (dest_station_id) REFERENCES stations(station_id) ON DELETE CASCADE ON UPDATE CASCADE,
                               CONSTRAINT fk_commute_origin FOREIGN KEY (origin_station_id) REFERENCES stations(station_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci COMMENT='通勤時間';

-- users : 会員
CREATE TABLE users (
                       user_id            BIGINT         NOT NULL AUTO_INCREMENT COMMENT '会員ID',
                       email              VARCHAR(100)   NOT NULL COMMENT 'メールアドレス',
                       password_hash      VARCHAR(255)   NOT NULL COMMENT 'パスワード',
                       role               VARCHAR(20)    NOT NULL DEFAULT 'USER' COMMENT '権限',
                       created_at         TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登録日時',
                       CONSTRAINT pk_users PRIMARY KEY (user_id),
                       CONSTRAINT uq_users_email UNIQUE (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci COMMENT='会員';

-- favorite_stations : お気に入り駅
CREATE TABLE favorite_stations (
                                   favorite_id        BIGINT         NOT NULL AUTO_INCREMENT COMMENT 'お気に入りID',
                                   user_id            BIGINT         NOT NULL COMMENT '会員ID',
                                   station_id         BIGINT         NOT NULL COMMENT '駅ID',
                                   created_at         TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登録日時',
                                   CONSTRAINT pk_favorite_stations PRIMARY KEY (favorite_id),
                                   CONSTRAINT uq_fav_user_station UNIQUE (user_id, station_id),
                                   KEY idx_fav_station (station_id),
                                   CONSTRAINT fk_fav_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
                                   CONSTRAINT fk_fav_station FOREIGN KEY (station_id) REFERENCES stations(station_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci COMMENT='お気に入り駅';

