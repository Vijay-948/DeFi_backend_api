package defi_backend_api.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

public class SecurityConfig {






    @Bean
    public FilterRegistrationBean<LogFilter> loggingFilter() {
        FilterRegistrationBean<LogFilter> registrationBean = new FilterRegistrationBean<>();

        LogFilter logFilter = new LogFilter();
        logFilter.setIncludeQueryString(true);
        logFilter.setIncludeHeaders(true);
        logFilter.setIncludePayload(true);
        logFilter.setMaxPayloadLength(10000);

        registrationBean.setFilter(logFilter);
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }
}
