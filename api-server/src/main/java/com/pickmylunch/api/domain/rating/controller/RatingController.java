package com.pickmylunch.api.domain.rating.controller;

import com.pickmylunch.api.domain.rating.dto.request.FindRatingListResponseDto;
import com.pickmylunch.api.domain.rating.dto.request.*;
import com.pickmylunch.api.domain.rating.dto.response.FindRatingListRequestDto;
import com.pickmylunch.api.domain.rating.dto.response.FindRatingResponseDto;
import com.pickmylunch.api.domain.rating.service.*;
import com.pickmylunch.api.global.security.details.AuthUser;
import com.pickmylunch.api.global.util.UrlHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurant/rates")
public class RatingController {

    private final RatingService ratingService;
    private final String REDIRECT_URL = "/api/restaurant";

    @PostMapping("/{restaurantId}")
    public ResponseEntity<Long> postRating(
            @PathVariable("restaurantId") String restaurantId,
            @RequestBody PostRatingRequestDto dto,
            @AuthenticationPrincipal AuthUser authUser) {
        Long result = ratingService.postRating(restaurantId, dto, authUser.getId());
        return ResponseEntity.created(UrlHelper.createUri(REDIRECT_URL, result)).body(result);
    }

    @PutMapping("/{ratingId}")
    public ResponseEntity<Long> putRating(
            @PathVariable("ratingId") Long ratingId,
            @RequestBody PutRatingRequestDto dto,
            @AuthenticationPrincipal AuthUser authUser) {
        Long result = ratingService.putRating(ratingId, dto, authUser.getId());
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{ratingId}")
    public ResponseEntity<Void> delRating(
            @PathVariable("ratingId") Long ratingId,
            @AuthenticationPrincipal AuthUser authUser) {
        ratingService.delRating(ratingId, authUser.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<FindRatingListResponseDto>> findRatingList(
            FindRatingListRequestDto dto, Pageable pageable) {
        return ResponseEntity.ok(ratingService.findRatingList(dto, pageable));
    }

    @GetMapping("/{ratingId}")
    public ResponseEntity<FindRatingResponseDto> findRatingDetail(
            @PathVariable("ratingId") Long ratingId) {
        return ResponseEntity.ok(ratingService.findRatingDetail(ratingId));
    }
}
