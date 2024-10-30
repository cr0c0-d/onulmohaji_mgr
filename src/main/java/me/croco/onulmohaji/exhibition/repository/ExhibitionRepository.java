package me.croco.onulmohaji.exhibition.repository;

import me.croco.onulmohaji.exhibition.domain.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExhibitionRepository extends JpaRepository<Exhibition, Long>, ExhibitionQueryDSLRepository {
}
