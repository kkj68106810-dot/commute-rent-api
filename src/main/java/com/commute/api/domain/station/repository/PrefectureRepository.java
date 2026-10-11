package com.commute.api.domain.station.repository;

import com.commute.api.domain.station.entity.Prefecture;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 都道府県エンティティのリポジトリインタフェース。
 * Repository interface for the Prefecture entity.
 *
 * @author Kim Gwangjin
 * @since 2026/09/27
 */
public interface PrefectureRepository extends JpaRepository<Prefecture, Long> {

    /**
     * 処理内容を記入する。
     * Write what this method does.
     *
     * @param name 説明 / Description
     * @return 戻り値の説明 / Return value description
     */
    Prefecture findByPrefectureName(String prefecture);
}
