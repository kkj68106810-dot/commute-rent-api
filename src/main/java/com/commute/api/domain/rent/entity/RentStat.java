package com.commute.api.domain.rent.entity;

import com.commute.api.domain.station.entity.Station;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "rent_stats", schema = "commute_rent", indexes = {@Index(name = "idx_rent_layout_avg",
        columnList = "room_layout, average_rent")}, uniqueConstraints = {@UniqueConstraint(name = "uq_rent_station_layout",
        columnNames = {
                "station_id",
                "room_layout"})})
public class RentStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rent_stat_id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @Size(max = 10)
    @NotNull
    @Column(name = "room_layout", nullable = false, length = 10)
    private String roomLayout;

    @NotNull
    @Column(name = "average_rent", nullable = false)
    private Integer averageRent;

    @Column(name = "min_rent")
    private Integer minRent;

    @Column(name = "max_rent")
    private Integer maxRent;

    @ColumnDefault("current_timestamp()")
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Builder
    public RentStat(Long id, Station station, String roomLayout, Integer averageRent, Integer minRent, Integer maxRent, Instant updatedAt) {
        this.id = id;
        this.station = station;
        this.roomLayout = roomLayout;
        this.averageRent = averageRent;
        this.minRent = minRent;
        this.maxRent = maxRent;
        this.updatedAt = updatedAt;
    }

    public RentStat() {

    }
}