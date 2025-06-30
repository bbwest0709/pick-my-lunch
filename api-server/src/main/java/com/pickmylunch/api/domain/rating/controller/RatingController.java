package com.pickmylunch.api.domain.rating.controller;

import com.pickmylunch.api.domain.rating.dto.request.PostRatingReqDto;
import com.pickmylunch.api.domain.rating.dto.request.PutRatingReqDto;
import com.pickmylunch.api.domain.rating.service.RatingService;
import com.pickmylunch.api.global.security.details.AuthUser;
import com.pickmylunch.api.global.util.UrlHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;
    private final String REDIRECT_URL = "/api/restaurant";

    @PostMapping("/api/restaurant/rates/{restaurantId}")
    public ResponseEntity<Long> postRating(
        @PathVariable("restaurantId") String restaurantId,
        @RequestBody PostRatingReqDto reqDto,
        @AuthenticationPrincipal AuthUser authUser) {
        Long result = ratingService.postRating(restaurantId, reqDto, authUser.getId());
        return ResponseEntity.created(UrlHelper.createUri(REDIRECT_URL, result)).body(result);
    }

    @PutMapping("/api/restaurant/rates/{ratingId}")
    public ResponseEntity<Long> putRating(
        @PathVariable("ratingId") Long ratingId,
        @RequestBody PutRatingReqDto reqDto,
        @AuthenticationPrincipal AuthUser authUser) {
        Long result = ratingService.putRating(ratingId, reqDto, authUser.getId());
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/api/restaurant/rates/{ratingId}")
    public ResponseEntity<Void> delRating(
        @PathVariable("ratingId") Long ratingId,
        @AuthenticationPrincipal AuthUser authUser) {
        ratingService.delRating(ratingId, authUser.getId());
        return ResponseEntity.noContent().build();
    }


}
