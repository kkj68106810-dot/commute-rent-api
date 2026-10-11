package com.commute.api.domain.station.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "rail_lines", schema = "commute_rent")
public class RailLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_id", nullable = false)
    private Long id;

    @Size(max = 20)
    @NotNull
    @Column(name = "line_name", nullable = false, length = 20)
    private String lineName;

    @Size(max = 100)
    @Column(name = "name_kana", length = 50)
    private String nameKana;

    @Size(max = 100)
    @Column(name = "name_en", length = 50)
    private String nameEn;


}