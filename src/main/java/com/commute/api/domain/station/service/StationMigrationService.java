package com.commute.api.domain.station.service;

import com.commute.api.common.utils.JapaneseConverter;
import com.commute.api.domain.station.dto.HeartRailsStationDto;
import com.commute.api.domain.station.entity.LineStation;
import com.commute.api.domain.station.entity.Prefecture;
import com.commute.api.domain.station.entity.RailLine;
import com.commute.api.domain.station.entity.Station;
import com.commute.api.domain.station.repository.LineStationRepository;
import com.commute.api.domain.station.repository.PrefectureRepository;
import com.commute.api.domain.station.repository.RailLineRepository;
import com.commute.api.domain.station.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * StationMigrationService
 *
 * @author Kim Gwangjin
 * @version 1.0.0
 * @since 2026/09/28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StationMigrationService {


    /** フィールド説明 / Field description */
    private final HeartRailsApiClient apiClient;
    private final RailLineRepository railLineRepository;
    private final StationRepository stationRepository;
    private final PrefectureRepository prefectureRepository;
    private final LineStationRepository lineStationRepository;
    private final JapaneseConverter japaneseConverter;


    /**
     * 処理内容を記入する。
     * Write what this method does.
     */
    @Transactional // 에러 발생 시 전체 롤백 보장
    public void migrateLineToStation() {
        log.info("[MIGRATION_START] Line to Station migration process has started.");
        long startTime = System.currentTimeMillis();

        try {
            List<RailLine> lineList = railLineRepository.findAll();
            if (lineList.isEmpty()) {
                log.warn("[MIGRATION_WARN] No lines found in DB. Migration aborted.");
                return;
            }

            Map<String, RailLine> lineMap = lineList.stream()
                    .collect(Collectors.toMap(RailLine::getLineName, line -> line));

            List<HeartRailsStationDto.StationInfo> stationList = new ArrayList<>();

            // 1단계: 외부 API 호출 구간 (네트워크 에러 가능성 존재)
            for (RailLine line : lineList) {
                try {
                    List<HeartRailsStationDto.StationInfo> stations = apiClient.getStations(line.getLineName());
                    stationList.addAll(stations);
                } catch (Exception e) {
                    // 특정 노선 조회 실패가 전체 마이그레이션을 죽이지 않도록 개별 처리하거나 상위로 던질 수 있음
                    log.error("[MIGRATION_ERR] Failed to fetch stations for line: {}. Error: {}", line.getLineName(), e.getMessage());
                    throw new RuntimeException("External API communication failed for line: " + line.getLineName(), e);
                }
            }

            Map<String, List<HeartRailsStationDto.StationInfo>> groupedStation = stationList.stream()
                    .collect(Collectors.groupingBy(HeartRailsStationDto.StationInfo::name));

            // 2단계: 데이터 가공 및 저장 구간
            for (Map.Entry<String, List<HeartRailsStationDto.StationInfo>> entry : groupedStation.entrySet()) {
                String stationName = entry.getKey();
                String katakana = japaneseConverter.convertKanjiToKatakana(stationName);
                List<HeartRailsStationDto.StationInfo> linesForStation = entry.getValue();

                HeartRailsStationDto.StationInfo firstLine = linesForStation.get(0);

                Station station = new Station();
                station.setStationName(stationName);
                station.setNameKana(katakana);
                station.setNameEn(japaneseConverter.convertKatakanaToEn(katakana));
                station.setLatitude(firstLine.y());
                station.setLongitude(firstLine.x());

                Prefecture prefecture = prefectureRepository.findByPrefectureName(firstLine.prefecture());
                if (prefecture == null) {
                    log.warn("[MIGRATION_WARN] Prefecture not found in DB: {}. Station: {}", firstLine.prefecture(), stationName);
                }
                station.setPrefecture(prefecture);

                Station savedStation = stationRepository.save(station);

                List<LineStation> mapping = linesForStation.stream()
                        .map(info -> {
                            RailLine matchedLine = lineMap.get(info.line());
                            LineStation lineStation = new LineStation();
                            lineStation.setLine(matchedLine);
                            lineStation.setStation(savedStation);
                            lineStation.setSequence(linesForStation.indexOf(info));
                            return lineStation;
                        }).toList();

                lineStationRepository.saveAll(mapping);
            }
            long elapsedTime = System.currentTimeMillis() - startTime;
            log.info("[MIGRATION_COMPLETE] Successfully processed {} stations. Elapsed Time: {}ms", groupedStation.size(), elapsedTime);

        } catch (Exception e) {
            log.error("[MIGRATION_FAIL] Critical error occurred during station migration: {}", e.getMessage(), e);
            throw new RuntimeException("Station migration failed", e);
        }
    }
}
