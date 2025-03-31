package com.demo.gateway.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpUtilsRequest {

    public final static String ALLOW_ALL = "*";
    public final static String ALLOW_ANY_START_WITH = "**";
    public final static String ALLOW_ANY_END_WITH = "**";

    public static boolean isIgnoreResources(String resource, String path) {
        log.trace("Ignore resources config:{}", resource);
        if (resource.startsWith(ALLOW_ANY_START_WITH) ||
                resource.endsWith(ALLOW_ANY_END_WITH)) {
            String resourcesRePlace = resource.replace("**", "");
            if (resource.startsWith(ALLOW_ANY_START_WITH)) {
                return path.endsWith(resourcesRePlace);
            } else {
                return path.startsWith(resourcesRePlace);
            }

        } else if (resource.equals(path)) {
            log.trace("Matched with with exactly path");
            return true;
        }
        log.trace("Ignore resource false!");
        return false;
    }
}
