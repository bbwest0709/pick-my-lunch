package com.pickmylunch.api.domain.region.service;

import com.pickmylunch.api.domain.region.repository.*;
import com.pickmylunch.api.domain.region.util.CSVHeaders;
import com.pickmylunch.api.global.exception.BusinessLogicException;
import com.pickmylunch.api.global.exception.code.CommonExceptionCode;
import com.pickmylunch.common.entity.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RegionService {

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

                String regionKey = String.format(REGION_KEY_FORMAT, dosi, sigungu);

                if (existingRegionKeys.contains(regionKey)) {
                    continue;
                }

                Region region = toEntity(dosi, sigungu, lon, lat);
                regions.add(region);
            }
            regionRepository.saveAll(regions);
        } catch (Exception e) {
            log.error("CSV 파싱 에러 : {}", e.getMessage());
            throw new BusinessLogicException(CommonExceptionCode.CSV_PARSING_ERROR);
        }
    }

    private Set<String> getExistingRegionKeys() {
        return regionRepository.findAllDosiAndSigungu().stream()
                .map(region -> String.format(REGION_KEY_FORMAT, region.getDosi(), region.getSigungu()))
                .collect(Collectors.toSet());
    }

    private static Region toEntity(String dosi, String sigungu, double lon, double lat) {
        return Region.builder()
                .dosi(dosi)
                .sigungu(sigungu)
                .lon(lon)
                .lat(lat)
                .build();
    }
}