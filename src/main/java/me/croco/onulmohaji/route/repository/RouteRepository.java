package me.croco.onulmohaji.route.repository;

import me.croco.onulmohaji.route.domain.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long>, RouteQueryDSLRepository {
}
