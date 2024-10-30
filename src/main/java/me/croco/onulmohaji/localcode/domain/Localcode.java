package me.croco.onulmohaji.localcode.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Localcode {

    @Id
    @Column
    private Long id;

    @Column(name = "local_level")
    private int localLevel;

    @Column(name = "parent_id")
    private Long parentId;

    @Column
    private String name;

    @Column
    private Double longitude;

    @Column
    private Double latitude;
}
