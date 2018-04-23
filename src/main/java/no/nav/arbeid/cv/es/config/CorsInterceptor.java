package no.nav.arbeid.cv.es.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

import static com.google.common.net.HttpHeaders.*;

@Component
public class CorsInterceptor extends HandlerInterceptorAdapter {

    private final List<String> allowedOrigins;

    @Inject
    public CorsInterceptor(
            @Value("${no.nav.arbeid.api.allowed.origins:http://localhost:9009,https://pam-cv-indexer.nais.oera-q.local,https://pam-cv-indexer-q.nav.no,https://pam-cv-indexer.nav.no}") String... allowedOrigins) {
        this(Arrays.asList(allowedOrigins));
    }

    public CorsInterceptor(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String origin = request.getHeader(ORIGIN);
        if (allowedOrigins.contains(origin)) {
            response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, origin);
            response.setHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [allowedOrigins=" + allowedOrigins + "]";
    }
}
