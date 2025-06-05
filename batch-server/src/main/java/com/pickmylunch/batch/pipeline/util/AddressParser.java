package com.pickmylunch.batch.pipeline.util;

import com.pickmylunch.batch.pipeline.dto.AddressDto;

public class AddressParser {
    public static AddressDto parse(String address) {
        if (address == null || address.isEmpty()) {
            return new AddressDto("", "", "");
        }
        String [] parts = address.split(" ", 3);
        return new AddressDto(
                getOrEmpty(parts, 0),
                getOrEmpty(parts, 1),
                getOrEmpty(parts, 2)
        );
    }

    private static String getOrEmpty(String[] array, int index) {
        return index < array.length ? array[index] : "";
    }
}
