package me.croco.onulmohaji.localcode.repository;

import me.croco.onulmohaji.localcode.domain.Localcode;

import java.util.List;

public interface LocalcodeQueryDSLRepository {
    List<Localcode> findLocalcodeList();
}
