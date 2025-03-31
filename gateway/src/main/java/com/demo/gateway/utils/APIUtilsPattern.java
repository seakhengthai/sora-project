package com.demo.gateway.utils;

import com.demo.gateway.domain.Status;
import com.demo.gateway.domain.entity.API;
import com.demo.gateway.exception.AppException;
import com.demo.gateway.exception.code.AppErrorCode;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.PathContainer;
import org.springframework.stereotype.Component;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.List;

@Slf4j
@Component("apiUtilPattern")
public class APIUtilsPattern {

    public static Tuple2<String, String> getServiceAndDownStreamUrl(String path) {
        String[] splitPath = path.split("/");
        String service = splitPath[1];
        String downstreamUrl = path.replace("/" + service, "");
        return Tuple.of(service, downstreamUrl);
    }

    @Cacheable(value = "apidefs", key = "{#method,#path}")
    public API getMatchingApi(String path, HttpMethod method, List<API> apiDefs) {
        Tuple2<String, String> servicePath = getServiceAndDownStreamUrl(path);
        API apiDef = apiDefs
                .stream()
                .filter(
                        api -> api.getStatus().equals(Status.ACTIVE) &&
                                this.isMethodAndServiceMatch(api, method, servicePath._1) &&
                                isMatchingURIPath(servicePath._2, api))
                .findFirst().orElseThrow(() -> new AppException(AppErrorCode.API_NOT_MATCHING, HttpStatus.BAD_REQUEST));
        log.info("Matching the API request from access path:[{}] and method: [{}] for code: [{}]", path, method,apiDef.getCode());
        return apiDef;
    }

    private boolean isMethodAndServiceMatch(API api, HttpMethod requestMethod,
                                            String backendContextService) {
        return api.getMethod().equals(requestMethod)
                && api.getService()
                .equalsIgnoreCase(backendContextService);
    }

    private boolean isMatchingURIPath(String downstreamUrl, API apiDef) {
        log.info("Downstream URL:{}, API config:{}", downstreamUrl, apiDef);
        PathPattern pathInDB = PathPatternParser.defaultInstance.parse(apiDef.getPath());
        PathContainer pathContainer = PathContainer.parsePath(downstreamUrl);
        return pathInDB.matches(pathContainer);
    }

}
