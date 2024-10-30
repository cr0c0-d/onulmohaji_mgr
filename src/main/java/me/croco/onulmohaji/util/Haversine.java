package me.croco.onulmohaji.util;

import me.croco.onulmohaji.facility.domain.Facility;

public class Haversine {
    
    // 두 지점 사이의 거리를 미터로 반환
    // 맨 앞의 (1000 *) 부분을 지우면 킬로미터
    public static Double getDistance(Double latitude_1, Double longitude_1, Double latitude_2, Double longitude_2) {
        return Math.ceil(1000 * 6371 * Math.acos(Math.cos(Math.toRadians(latitude_1)) * Math.cos(Math.toRadians(latitude_2)) * Math.cos(Math.toRadians(longitude_2) - Math.toRadians(longitude_1)) + Math.sin(Math.toRadians(latitude_1)) * Math.sin(Math.toRadians(latitude_2))));
    }

    public static Double getDistance(Double latitude, Double longitude, Facility facility) {
        return getDistance(latitude, longitude, facility.getLatitude(), facility.getLongitude());
    }
}
