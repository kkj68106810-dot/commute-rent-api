package com.commute.api.domain.commute.entity;

import com.commute.api.domain.station.entity.Station;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * クラスの説明を記入する。
 * Write the class description.
 *
 * @author Kim Gwangjin
 * @since 2026/10/20
 */
@Getter
@Setter
@Entity
@Table(name = "commute_times", schema = "commute_rent", indexes = {
        @Index(name = "idx_commute_dest_min",
                columnList = "dest_station_id, minutes"),
        @Index(name = "idx_commute_origin",
                columnList = "origin_station_id")})
public class CommuteTime {
    @EmbeddedId
    private CommuteTimeId id;

    @MapsId("destStationId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "dest_station_id", nullable = false)
    private Station destStation;

    @MapsId("originStationId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "origin_station_id", nullable = false)
    private Station originStation;

    @NotNull
    @Column(name = "minutes", nullable = false)
    private Short minutes;

    @NotNull
    @Column(name = "transfers", nullable = false)
    private Byte transfers;

}