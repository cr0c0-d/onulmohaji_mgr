package me.croco.onulmohaji.popupstore.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.croco.onulmohaji.popupstore.domain.Popupstore;
import me.croco.onulmohaji.popupstore.domain.QPopupstore;
import me.croco.onulmohaji.popupstore.domain.QPopupstoreDetail;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
public class PopupstoreQueryDSLRepositoryImpl implements PopupstoreQueryDSLRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private final QPopupstore qPopupstore = QPopupstore.popupstore;
    private final QPopupstoreDetail qPopupstoreDetail = QPopupstoreDetail.popupstoreDetail;

    // 주어진 위도,경도 좌표에서부터 거리를 구함
    private NumberTemplate<Double> haversineFormula(Double latitude, Double longitude) {
        return Expressions.numberTemplate(Double.class,
                "6371 * acos(cos(radians({0})) * cos(radians({1}.latitude)) * cos(radians({1}.longitude) - radians({2})) + sin(radians({0})) * sin(radians({1}.latitude)))",
                latitude, qPopupstore, longitude);
    }

    @Override
    public List<Popupstore> findPopupstoreListByDate(String keyword, String date, Double latitude, Double longitude, int distance) {
        BooleanExpression isBefore = qPopupstore.startDate.lt(date).or(qPopupstore.startDate.eq(date));
        BooleanExpression isAfter = qPopupstore.endDate.gt(date).or(qPopupstore.endDate.eq(date));

        return jpaQueryFactory.selectFrom(qPopupstore)
                .where(isBefore.and(isAfter)
                        .and(keyword == null ? Expressions.asBoolean(true).isTrue() :
                                qPopupstore.title.contains(keyword)
                                .or(qPopupstore.brand_1.contains(keyword))
                                .or(qPopupstore.hashtag.contains(keyword))
                                .or(qPopupstore.name.contains(keyword))
                                .or(qPopupstore.searchItems.contains(keyword))
                                .or(qPopupstoreDetail.contents.contains(keyword))
                                .or(qPopupstore.address.contains(keyword))
                        )
                        .and(haversineFormula(latitude, longitude).lt(distance/1000))
                )
                .join(qPopupstoreDetail)
                .on(qPopupstore.storeId.eq(qPopupstoreDetail.storeId))
                .orderBy(haversineFormula(latitude, longitude).asc())
                .fetch();
    }
}
