package me.croco.onulmohaji.localcode.service;

import lombok.RequiredArgsConstructor;
import me.croco.onulmohaji.localcode.domain.Localcode;
import me.croco.onulmohaji.localcode.repository.LocalcodeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocalcodeService {

    private final LocalcodeRepository localcodeRepository;

    public List<Localcode> findLocalcodeList() {
        return localcodeRepository.findLocalcodeList();
    }

    public Localcode findById(Long id) {
        return localcodeRepository.findById(id).get();
    }
}
