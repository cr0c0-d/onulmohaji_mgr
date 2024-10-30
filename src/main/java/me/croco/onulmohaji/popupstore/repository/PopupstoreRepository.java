package me.croco.onulmohaji.popupstore.repository;

import me.croco.onulmohaji.popupstore.domain.Popupstore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopupstoreRepository extends JpaRepository<Popupstore, Long>, PopupstoreQueryDSLRepository {
}
