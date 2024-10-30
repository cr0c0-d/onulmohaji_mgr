package me.croco.onulmohaji.exhibition.repository;

import me.croco.onulmohaji.exhibition.domain.Exhibition;

import java.util.List;

public interface ExhibitionQueryDSLRepository {
    List<Exhibition> findExhibitionListByDate(String keyword, String date, Double latitude, Double longitude, int distance);

}
