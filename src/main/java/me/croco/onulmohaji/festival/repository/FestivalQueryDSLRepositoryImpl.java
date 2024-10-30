package me.croco.onulmohaji.festival.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.croco.onulmohaji.festival.domain.Festival;
import me.croco.onulmohaji.festival.domain.QFestival;

import java.util.List;

@RequiredArgsConstructor
public class FestivalQueryDSLRepositoryImpl implements FestivalQueryDSLRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private QFestival qFestival = QFestival.festival;

    // 주어진 위도,경도 좌표에서부터 거리를 구함
    private NumberTemplate<Double> haversineFormula(Double latitude, Double longitude) {
        return Expressions.numberTemplate(Double.class,
                "6371 * acos(cos(radians({0})) * cos(radians({1}.latitude)) * cos(radians({1}.longitude) - radians({2})) + sin(radians({0})) * sin(radians({1}.latitude)))",
                latitude, qFestival, longitude);
    }

    @Override
    public List<Festival> findXyNullFestivals() {
        return jpaQueryFactory.selectFrom(qFestival)
                .where(qFestival.latitude.isNull().or(qFestival.longitude.isNull()))
                .fetch();
    }

    @Override
    public List<Festival> findFestivalListByDate(String keyword, String date, Double latitude, Double longitude, int distance) {
        BooleanExpression isBefore = qFestival.startDate.lt(date).or(qFestival.startDate.eq(date));
        BooleanExpression isAfter = qFestival.endDate.gt(date).or(qFestival.endDate.eq(date));

        return jpaQueryFactory.selectFrom(qFestival)
                .where(isBefore.and(isAfter)
                        .and(keyword == null ? Expressions.asBoolean(true).isTrue() :
                                qFestival.title.contains(keyword)
                                        .or(qFestival.contents1.contains(keyword))
                                        .or(qFestival.contents2.contains(keyword))
                                        .or(qFestival.title.contains(keyword))
                                        .or(qFestival.address.contains(keyword))
                        )
                        .and(haversineFormula(latitude, longitude).lt(distance/1000))
                )
                .orderBy(haversineFormula(latitude, longitude).asc())
                .fetch();
    }


}
