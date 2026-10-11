package com.commute.api.domain.station.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * HeartRails駅情報レスポンスDTO。
 * HeartRails station information response DTO.
 *
 * @param response 駅レスポンス本体 / Station response body
 * @author Kim Gwangjin
 * @since 2026/09/27
 */
public record HeartRailsStationDto(StationResponse response) {

    /**
     * 駅リストを保持するレスポンスレコード。
     * Response record that holds the station list.
     *
     * @param station 駅情報リスト / List of station information
     */
    public record StationResponse(List<StationInfo> station) {}

    /**
     * 個別駅情報レコード。
     * Individual station information record.
     *
     * @param name       駅名 / Station name
     * @param prefecture 所属都道府県名 / Belonging prefecture name
     * @param line       所属路線名 / Belonging line name
     * @param x          経度（Longitude） / Longitude
     * @param y          緯度（Latitude） / Latitude
     */
    public record StationInfo(
            String name,
            String prefecture,
            String line,
            BigDecimal x,
            BigDecimal y
    ) {}
}
