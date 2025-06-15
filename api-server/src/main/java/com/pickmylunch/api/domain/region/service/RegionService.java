package com.pickmylunch.api.domain.region.service;

import com.pickmylunch.api.domain.region.dto.RegionDto;
import com.pickmylunch.api.domain.region.repository.RegionRepository;
import com.pickmylunch.common.entity.Region;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;

    @Transactional(readOnly = true)
    public List<RegionDto> getAllRegions() {
        return regionRepository.findAll().stream()
                .map(RegionDto::from)
                .toList();
    }
}
