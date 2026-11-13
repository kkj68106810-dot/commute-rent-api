package com.commute.api.domain.station.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "prefectures", schema = "commute_rent")
public class Prefecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prefecture_id", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "prefecture_name", nullable = false, length = 50)
    private String prefectureName;

    @Size(max = 200)
    @Column(name = "name_kana", length = 200)
    private String nameKana;

    @Size(max = 200)
    @Column(name = "name_en", length = 200)
    private String nameEn;
    @OneToMany(mappedBy = "prefecture")
    private Set<Station> stations = new LinkedHashSet<>();


}