package com.commute.api.domain.rent.repository;

import com.commute.api.domain.rent.entity.RentStat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentStatRepository extends JpaRepository<RentStat, Long> {

}
