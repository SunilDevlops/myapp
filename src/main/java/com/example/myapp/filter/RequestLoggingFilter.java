package com.example.myapp.filter;

import jakarta.servlet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RequestLoggingFilter implements Filter, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        logger.info("Inside RequestLogging filter - doFilterInternal method");

        String podName = System.getenv("HOSTNAME");  // Get the pod name
        logger.debug("Request is being handled by pod: " + podName);  // Log to console or use a logger

        chain.doFilter(request, response);  // Continue the request
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Optional: Init logic
    }

    @Override
    public void destroy() {
        // Optional: Destroy logic
    }

    @Override
    public int getOrder() {
        return 1;
    }
}