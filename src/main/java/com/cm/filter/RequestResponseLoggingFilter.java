package com.cm.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@ConditionalOnProperty(
        name = "logging.request-response.enabled",
        havingValue = "true"
)
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private static final Logger REQ_RES_LOGGER = LoggerFactory.getLogger("REQUEST_RESPONSE_LOGGER");


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().startsWith("/api");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request, 1024);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        String requestId = UUID.randomUUID().toString().substring(0, 8);
        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            long durationMs = System.currentTimeMillis() - startTime;
            // Logged AFTER the chain completes: the request body is only populated
            // in the cache once something downstream (e.g. Jackson/@RequestBody) has
            // actually read the input stream.
            logRequest(wrappedRequest, requestId);
            logResponse(wrappedResponse, requestId, durationMs);
            // Required so the actual client still receives the response body,
            // since ContentCachingResponseWrapper buffers it internally.
            wrappedResponse.copyBodyToResponse();
        }
    }

    private void logRequest(ContentCachingRequestWrapper request, String requestId) {
        REQ_RES_LOGGER.info("REQUEST  [{}] | {} {} |  Body: {}",
                requestId,
                request.getMethod(),
                request.getRequestURI(),
                getBody(request.getContentAsByteArray()));
    }

    private void logResponse(ContentCachingResponseWrapper response, String requestId, long durationMs) {
        REQ_RES_LOGGER.info("RESPONSE [{}] | Status: {} | Duration: {}ms | Body: {}",
                requestId,
                response.getStatus(),
                durationMs,
                getBody(response.getContentAsByteArray()));
    }

    private String getBody(byte[] body) {
        if (body.length == 0) {
            return "";
        }

        return new String(body, StandardCharsets.UTF_8)
                .replaceAll("\\s+", " ")
                .trim();
    }

}
