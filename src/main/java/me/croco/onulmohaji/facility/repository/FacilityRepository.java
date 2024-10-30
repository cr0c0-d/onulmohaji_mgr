package me.croco.onulmohaji.facility.repository;

import me.croco.onulmohaji.facility.domain.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilityRepository extends JpaRepository<Facility, Long>, FacilityQueryDSLRepository {
}
