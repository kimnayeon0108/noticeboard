package com.example.noticeboard.security.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public enum JwtAuthenticationRequestMatcher {

    CATEGORY_PATH(null, "/categories/**"),
    POST_PATH_POST(HttpMethod.POST, "/posts/**"),
    POST_PATH_PUT(HttpMethod.PUT, "/posts/**"),
    POST_PATH_DELETE(HttpMethod.DELETE, "/posts/**"),
    USER_PATH(HttpMethod.GET, "/users/**"),
    REFRESH_TOKEN_PATH_POST(HttpMethod.POST, "/users/authorize");

    private HttpMethod method;
    private String url;

    public static List<RequestMatcher> getMatchers() {
        List<RequestMatcher> requestMatchers = new ArrayList<>();

        for (JwtAuthenticationRequestMatcher value : JwtAuthenticationRequestMatcher.values()) {
            if (value == REFRESH_TOKEN_PATH_POST) {
                continue;
            }
            requestMatchers.add(new AntPathRequestMatcher(value.url, value.method != null ? value.method.name() : null));
        }

        return requestMatchers;
    }
}
