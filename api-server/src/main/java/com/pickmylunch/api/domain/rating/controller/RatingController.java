package com.pickmylunch.api.domain.rating.controller;

import com.pickmylunch.api.domain.rating.dto.request.PostRatingReqDto;
import com.pickmylunch.api.domain.rating.service.RatingService;
import com.pickmylunch.api.global.security.details.AuthUser;
import com.pickmylunch.api.global.util.UrlHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;
    private final String REDIRECT_URL = "/api/restaurant";

    @PostMapping("/api/restaurant/rate/{restaurantId}")
    public ResponseEntity<Long> postRating(
        @PathVariable("restaurantId") Long restaurantId,
        @RequestBody PostRatingReqDto reqDto,
        @AuthenticationPrincipal AuthUser authUser) {
        Long result = ratingService.postRating(restaurantId, reqDto, authUser.getId());
        return ResponseEntity.created(UrlHelper.createUri(REDIRECT_URL, result)).body(result);
    }
}
