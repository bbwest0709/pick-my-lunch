package com.pickmylunch.api.domain.region.service;

import com.pickmylunch.api.domain.region.dto.RegionDto;
import com.pickmylunch.api.domain.region.repository.RegionRepository;
import com.pickmylunch.api.domain.region.util.CSVHeaders;
import com.pickmylunch.api.global.exception.BusinessLogicException;
import com.pickmylunch.api.global.exception.code.CommonExceptionCode;
import com.pickmylunch.common.entity.Region;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CSVRegionService {

    private final RegionRepository regionRepository;

    private static final String REGION_KEY_FORMAT = "%s_%s";

    @PostConstruct
    public void importRegionsFromCSV() {
        ClassPathResource resource = new ClassPathResource("region/seoul_district_centroids_2017.csv");

        try (
                InputStream csvInputStream = resource.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(csvInputStream, StandardCharsets.UTF_8));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())
        ) {
            Set<String> existingRegionKeys = getExistingRegionKeys();
            List<Region> regions = new ArrayList<>();

            for (CSVRecord record : csvParser) {
                String dosi = record.get(CSVHeaders.DOSI);
                String sigungu = record.get(CSVHeaders.SIGUNGU);
                double lon = Double.parseDouble(record.get(CSVHeaders.LON));
                double lat = Double.parseDouble(record.get(CSVHeaders.LAT));

                String regionKey = createRegionKey(dosi, sigungu);
                if (existingRegionKeys.contains(regionKey)) {
                    continue;
                }

                RegionDto dto = new RegionDto(dosi, sigungu, lon, lat);
                regions.add(RegionDto.of(dto));
            }
            regionRepository.saveAll(regions);
        } catch (Exception e) {
            log.error("CSV 파싱 에러 : {}", e.getMessage());
            throw new BusinessLogicException(CommonExceptionCode.CSV_PARSING_ERROR);
        }
    }

    private Set<String> getExistingRegionKeys() {
        return regionRepository.findAllDosiAndSigungu().stream()
                .map(region -> createRegionKey(region.getDosi(), region.getSigungu()))
                .collect(Collectors.toSet());
    }

    private static String createRegionKey(String dosi, String sigungu) {
        return String.format(
                REGION_KEY_FORMAT,
                dosi.trim().toLowerCase(),
                sigungu.trim().toLowerCase());
    }
}