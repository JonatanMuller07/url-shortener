package com.mercado.libre.url_shortener.util;

import org.apache.commons.lang3.RandomStringUtils;

public class UrlUtil {

    private static final String BASE_URL = "goo.gl/";

    public static String shortenUrl() {
        String shortCode = RandomStringUtils.randomAlphanumeric(6);
        return BASE_URL + shortCode;
    }
}
