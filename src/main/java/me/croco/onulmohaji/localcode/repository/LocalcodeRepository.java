package me.croco.onulmohaji.localcode.repository;

import me.croco.onulmohaji.localcode.domain.Localcode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalcodeRepository extends JpaRepository<Localcode, Long>, LocalcodeQueryDSLRepository {
}
