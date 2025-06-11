package com.pickmylunch.batch.pipeline.step;

import com.pickmylunch.batch.pipeline.repository.*;
import com.pickmylunch.common.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.StreamSupport;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestaurantItemWriter implements ItemWriter<Restaurant> {

    private final RestaurantRepository restaurantRepository;
    private final RawRestaurantRepository rawRestaurantRepository;

    @Override
    public void write(Chunk<? extends Restaurant> restaurants) {
        if (restaurants.isEmpty()) {
            return;
        }
        restaurantRepository.saveAll(restaurants);

        List<String> ids = StreamSupport.stream(restaurants.spliterator(), false)
                .map(Restaurant::getId)
                .toList();

        rawRestaurantRepository.updateIsUpdatedFalseByIds(ids);

        log.info("[success] RestaurantItemWriter {}건 저장", restaurants.size());
    }
}
