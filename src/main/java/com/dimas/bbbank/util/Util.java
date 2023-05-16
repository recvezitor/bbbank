package com.dimas.bbbank.util;

import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.util.Assert.notNull;

public class Util {

    public static final String SCHEMA_NAME = "bbbank";
    public static final String ROOT_PATH = "/api/v1";
    public static final String TIMESTAMP_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    public static final String API_ERROR_LBL_TIMESTAMP = "timestamp";
    public static final String API_ERROR_LBL_PATH = "path";
    public static final String API_ERROR_LBL_STATUS = "status";
    public static final String API_ERROR_LBL_ERRORS = "errors";
    public static final String API_ERROR_LBL_MESSAGE = "message";
    public static final String CACHE_NAME_EMAILS = "emails";
    public static final String CACHE_NAME_PHONES = "phones";


    public static String mapToString(Map<String, String> map) {
        notNull(map, "map cannot be null");
        return map.keySet().stream()
                .map(key -> key + ":" + map.get(key))
                .collect(Collectors.joining(", "));
    }

}
