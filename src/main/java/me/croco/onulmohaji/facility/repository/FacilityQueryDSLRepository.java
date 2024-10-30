package me.croco.onulmohaji.facility.repository;

import com.querydsl.core.Tuple;
import me.croco.onulmohaji.facility.domain.Facility;

import java.util.List;

public interface FacilityQueryDSLRepository {
    List<Facility> findFoodListByPlace(Double latitude, Double longitude);

    List<Facility> findDefaultFacilityList(Double latitude, Double longitude);

    List<Facility> findFacilityListByKeyword(String keyword, Double latitude, Double longitude);

    List<Facility> findFacilityListByCategory(String type, Double latitude, Double longitude, int distance);
}
