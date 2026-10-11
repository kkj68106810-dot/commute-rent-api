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
@Table(name = "prefectures", schema = "commute_rent")
public class Prefecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prefecture_id", nullable = false)
    private Long id;

    @Size(max = 20)
    @NotNull
    @Column(name = "prefecture_name", nullable = false, length = 20)
    private String prefectureName;

    @Size(max = 100)
    @Column(name = "name_kana", length = 50)
    private String nameKana;

    @Size(max = 100)
    @Column(name = "name_en", length = 50)
    private String nameEn;


}