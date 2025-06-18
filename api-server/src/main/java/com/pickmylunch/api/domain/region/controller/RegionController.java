package com.pickmylunch.api.domain.region.controller;

import com.pickmylunch.api.domain.region.dto.*;
import com.pickmylunch.api.domain.region.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/region")
public class RegionController {

    private final RegionService regionService;

    @GetMapping
    public ResponseEntity<List<RegionDto>> getRegions() {
        return ResponseEntity.ok(regionService.getAllRegions());
    }

    @GetMapping("/search")
    public ResponseEntity<RegionDto> searchRegions(
            @RequestParam(required = false) String sigungu) {
        return ResponseEntity.ok(regionService.searchRegions(sigungu));
    }

}
