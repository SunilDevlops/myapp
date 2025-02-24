package com.example.myapp.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;
@Component
public class RequestFilter extends OncePerRequestFilter {
    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("Inside request filter - doFilterInternal method");
        // Step 1: Generate a unique requestId for the current request
        String requestId = UUID.randomUUID().toString();
        System.out.println("requestId : "+requestId);

        // Step 2: Capture the request URL (path)
        String requestUrl = request.getRequestURI();
        System.out.println("requestUrl : "+requestUrl);
        System.out.println("activeProfile : "+activeProfile);

        // Step 3: Set the requestId, environment, and path in MDC (Mapped Diagnostic Context)
        MDC.put("requestId", requestId); // Unique identifier for the request
        MDC.put("environment", activeProfile); // Active profile like "dev", "prod", etc.
        MDC.put("path", requestUrl); // The URL or path of the current request

        // Step 4: Continue the filter chain (important to let the request proceed)
        filterChain.doFilter(request, response);

        // Step 5: Clear MDC after request processing (important to avoid memory leaks in multi-threaded environments)
        MDC.clear();
    }

}
