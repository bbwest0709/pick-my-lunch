package com.pickmylunch.batch.pipeline.step;

import com.pickmylunch.batch.pipeline.service.*;
import com.pickmylunch.common.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestaurantItemProcessor implements ItemProcessor<RawRestaurant, Restaurant> {

    private final RestaurantProcessor restaurantProcessor;

    @Override
    public Restaurant process(RawRestaurant rawRestaurant) throws Exception {
        if (!restaurantProcessor.shouldUpdate(rawRestaurant)) return null;
        return restaurantProcessor.convertToProcessedRestaurant(rawRestaurant);
    }
}
