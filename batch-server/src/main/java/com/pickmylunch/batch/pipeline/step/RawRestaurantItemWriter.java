package com.pickmylunch.batch.pipeline.step;

import com.pickmylunch.batch.pipeline.repository.*;
import com.pickmylunch.common.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RawRestaurantItemWriter implements ItemWriter<RawRestaurant> {

    private final RawRestaurantRepository rawRestaurantRepository;

    @Override
    public void write(Chunk<? extends RawRestaurant> rawRestaurants) throws Exception {
        rawRestaurantRepository.saveAll(rawRestaurants);
    }
}
