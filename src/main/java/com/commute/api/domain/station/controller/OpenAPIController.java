package com.commute.api.domain.station.controller;

import com.commute.api.common.utils.JapaneseConverter;
import com.commute.api.domain.station.entity.Prefecture;
import com.commute.api.domain.station.entity.RailLine;
import com.commute.api.domain.station.entity.Station;
import com.commute.api.domain.station.repository.PrefectureRepository;
import com.commute.api.domain.station.repository.RailLineRepository;
import com.commute.api.domain.station.repository.StationRepository;
import com.commute.api.domain.station.service.HeartRailsApiClient;
import com.commute.api.domain.station.service.StationMigrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * HeartRails外部APIから駅関連マスタを取得し、DBへ登録するコントローラー。
 * Controller that fetches station-related master data from HeartRails external API and persists it to the DB.
 *
 * @author Kim Gwangjin
 * @since 2026/09/27
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/station")
@CrossOrigin(origins = "http://localhost:3000")
public class OpenAPIController {

    /**
     * HeartRails APIクライアント / HeartRails API client
     */
    private final HeartRailsApiClient heartRailsApiClient;

    /**
     * 都道府県リポジトリ / Prefecture repository
     */
    private final PrefectureRepository prefectureRepository;

    /**
     * 路線リポジトリ / Line repository
     */
    private final RailLineRepository railLineRepository;

    /**
     * 駅リポジトリ / Station repository
     */
    private final StationRepository stationRepository;
    private final StationMigrationService stationMigrationService;
    private final JapaneseConverter japaneseConverter;

    /**
     * 全国の都道府県一覧をHeartRailsから取得し、DBへ一括登録する。
     * Fetches the nationwide prefecture list from HeartRails and bulk-saves it to the DB.
     */
    @GetMapping("/prefecture/all")
    public void getPrefectures() {
        long startTime = System.currentTimeMillis();

        log.info("[API_REQ] Fetching all prefectures from HeartRails external API.");
        try {
            List<String> prefectureList = heartRailsApiClient.getPrefectures();

            List<Prefecture> prefectures = prefectureList.stream().map(s ->
                    {
                        String katakana = japaneseConverter.convertKanjiToKatakana(s);
                        Prefecture pref = new Prefecture();
                        pref.setPrefectureName(s);
                        pref.setNameKana(katakana);
                        pref.setNameEn(japaneseConverter.convertKatakanaToEn(katakana));
                        return pref;
                    })
                    .toList();
            prefectureRepository.saveAll(prefectures);

            long elapsedTime = System.currentTimeMillis() - startTime;
            //
            log.info("[API_RES] Successfully saved {} prefectures. Elapsed Time: {}ms", prefectures.size(), elapsedTime);
        } catch (Exception e) {
            //
            log.error("[API_ERR] Failed to fetch and save prefectures: {}", e.getMessage(), e);
            throw e; //
        }
    }

    /**
     * DB上の全都道府県について路線一覧を取得し、DBへ一括登録する。
     * For every prefecture in the DB, fetches the line list and bulk-saves it to the DB.
     */
    @GetMapping("/line/all")
    public void getLines() {
        long startTime = System.currentTimeMillis();
        log.info("[API_REQ] Starting line migration process from DB prefectures.");

        List<Prefecture> prefList = prefectureRepository.findAll();
        log.info("Found {} prefectures in DB. Fetching corresponding lines...", prefList.size());
        try {
            List<RailLine> lines = prefList.stream()
                    .flatMap(pref -> heartRailsApiClient.getLines(pref.getPrefectureName()).stream())
                    .distinct()
                    .map(lineName -> {

                        String katakana = japaneseConverter.convertKanjiToKatakana(lineName);

                        RailLine newLine = new RailLine();
                        newLine.setLineName(lineName);
                        newLine.setNameKana(katakana);
                        newLine.setNameEn(japaneseConverter.convertKatakanaToEn(katakana));
                        return newLine;
                    })
                    .toList();

            railLineRepository.saveAll(lines);
            long elapsedTime = System.currentTimeMillis() - startTime;

            log.info("[API_RES] Successfully saved {} unique lines. Elapsed Time: {}ms", lines.size(), elapsedTime);
        } catch (Exception e) {
            //
            log.error("[API_ERR] Failed to fetch and save lines: {}", e.getMessage(), e);
            throw e; //
        }
    }

    /**
     * DB上の全路線について駅一覧を取得し、DBへ一括登録する。 Station linetoStation
     * For every line in the DB, fetches the station list and bulk-saves it to the DB.
     *
     * <p>現在はコメントアウト中。 / Currently commented out.</p>
     */
    @GetMapping("/line/station/all")
    public void getLineToStations() {
        long startTime = System.currentTimeMillis();
        log.info("[API_REQ] Starting line-to-station migration service.");

        try {
            stationMigrationService.migrateLineToStation();
            long elapsedTime = System.currentTimeMillis() - startTime;
            log.info("[API_RES] Line-to-station migration completed successfully. Elapsed Time: {}ms", elapsedTime);
        } catch (Exception e) {
            log.error("[API_ERR] Line-to-station migration failed: {}", e.getMessage(), e);
            throw e;
        }
    }

}
