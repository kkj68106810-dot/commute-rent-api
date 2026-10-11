package com.commute.api.domain.station.repository;

import com.commute.api.domain.station.entity.RailLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RailLineRepository extends JpaRepository<RailLine, Long> {
}
