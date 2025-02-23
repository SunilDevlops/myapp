/*
package com.example.myapp.config;

import com.example.myapp.filter.RequestFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<RequestFilter> loggingFilter() {
        System.out.println("Inside request filter - doFilterInternal method");
        FilterRegistrationBean<RequestFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestFilter()); // set the custom filter
        registrationBean.addUrlPatterns("/api/customers/*");  // Apply filter only on specific URLs
        registrationBean.setOrder(1); // set the order of filter
        System.out.println("RegistrationBean : "+registrationBean.getUrlPatterns());
        return registrationBean;
    }
}
*/
