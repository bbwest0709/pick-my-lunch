package com.pickmylunch.api.domain.region.service;

import com.pickmylunch.api.domain.region.dto.*;
import com.pickmylunch.api.domain.region.repository.*;
import com.pickmylunch.common.entity.Region;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;

    public List<RegionDto> getAllRegions() {
        return regionRepository.findAll().stream()
                .map(RegionDto::from)
                .toList();
    }

    public RegionDto searchRegions(String sigungu) {
        Region region = regionRepository.findBySigungu(sigungu);
        return region != null ? RegionDto.from(region) : null;
    }
}
