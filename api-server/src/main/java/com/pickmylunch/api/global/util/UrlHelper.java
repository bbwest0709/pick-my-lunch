package com.pickmylunch.api.global.util;

import java.net.URI;
import org.springframework.web.util.UriComponentsBuilder;

public class UrlHelper {

    public static URI createUri(String defaultUrl, long resourceId) {
        return UriComponentsBuilder
            .newInstance()
            .path(defaultUrl + "/{resource-id}")
            .buildAndExpand(resourceId)
            .toUri();
    }

    public static URI createUri(String defaultUrl) {
        return UriComponentsBuilder
            .newInstance()
            .path(defaultUrl)
            .build()
            .toUri();
    }

}
