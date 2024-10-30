package me.croco.onulmohaji.localcode.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.croco.onulmohaji.localcode.domain.Localcode;
import me.croco.onulmohaji.localcode.domain.QLocalcode;

import java.util.List;

@RequiredArgsConstructor
public class LocalcodeQueryDSLRepositoryImpl implements LocalcodeQueryDSLRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private QLocalcode qLocalcode = QLocalcode.localcode;

    @Override
    public List<Localcode> findLocalcodeList() {
        return jpaQueryFactory.selectFrom(qLocalcode)
                .orderBy(qLocalcode.name.asc())
                .fetch();
    }
}
