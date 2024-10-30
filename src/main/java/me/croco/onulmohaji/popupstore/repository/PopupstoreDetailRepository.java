package me.croco.onulmohaji.popupstore.repository;

import me.croco.onulmohaji.popupstore.domain.PopupstoreDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PopupstoreDetailRepository extends JpaRepository<PopupstoreDetail, Long> {
    Optional<PopupstoreDetail> findByStoreId(Long id);
}
