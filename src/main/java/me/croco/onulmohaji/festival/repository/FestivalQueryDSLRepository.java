package me.croco.onulmohaji.festival.repository;

import me.croco.onulmohaji.festival.domain.Festival;

import java.util.List;

public interface FestivalQueryDSLRepository {

    List<Festival> findXyNullFestivals();
    List<Festival> findFestivalListByDate(String keyword, String date, Double latitude, Double longitude, int distance);
}
