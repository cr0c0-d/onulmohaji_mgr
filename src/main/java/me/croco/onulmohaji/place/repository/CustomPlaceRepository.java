package me.croco.onulmohaji.place.repository;

import me.croco.onulmohaji.place.domain.CustomPlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomPlaceRepository extends JpaRepository<CustomPlace, Long> {
    List<CustomPlace> findCustomPlaceListByUserId(Long userId);
}
