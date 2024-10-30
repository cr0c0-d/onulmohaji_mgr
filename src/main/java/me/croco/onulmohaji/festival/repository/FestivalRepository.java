package me.croco.onulmohaji.festival.repository;

import me.croco.onulmohaji.festival.domain.Festival;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FestivalRepository extends JpaRepository<Festival, String>, FestivalQueryDSLRepository {
}
