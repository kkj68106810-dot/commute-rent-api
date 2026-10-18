package com.commute.api.domain.station.entity;

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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "stations", schema = "commute_rent", indexes = {
        @Index(name = "idx_stations_prefecture",
                columnList = "prefecture_id"),
        @Index(name = "idx_stations_lat_lng",
                columnList = "latitude, longitude")})
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "station_id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "prefecture_id", nullable = false)
    private Prefecture prefecture;

    @Size(max = 50)
    @NotNull
    @Column(name = "station_name", nullable = false, length = 50)
    private String stationName;

    @Size(max = 100)
    @Column(name = "name_kana", length = 100)
    private String nameKana;

    @Size(max = 100)
    @Column(name = "name_en", length = 100)
    private String nameEn;

    @NotNull
    @Column(name = "latitude", nullable = false, precision = 10, scale = 8)
    private BigDecimal latitude;

    @NotNull
    @Column(name = "longitude", nullable = false, precision = 11, scale = 8)
    private BigDecimal longitude;

    @NotNull
    @Column(name = "is_major_workspace", nullable = false)
    @ColumnDefault("false")
    private boolean isMajorWorkspace;

}