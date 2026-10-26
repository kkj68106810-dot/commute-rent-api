package com.commute.api.domain.commute.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * クラスの説明を記入する。
 * Write the class description.
 *
 * @author Kim Gwangjin
 * @since 2026/10/20
 */
@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class CommuteTimeId implements Serializable {

    @Serial
    private static final long serialVersionUID = 2197315152451507889L;
    @NotNull
    @Column(name = "dest_station_id", nullable = false)
    private Long destStationId;

    @NotNull
    @Column(name = "origin_station_id", nullable = false)
    private Long originStationId;


}