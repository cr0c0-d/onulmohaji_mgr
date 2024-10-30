package me.croco.onulmohaji.popupstore.repository;

import me.croco.onulmohaji.popupstore.domain.PopupstoreImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PopupstoreImageRepository extends JpaRepository<PopupstoreImage, Long> {
    List<PopupstoreImage> findByStoreIdOrderByStoreImageId(Long id);
}
