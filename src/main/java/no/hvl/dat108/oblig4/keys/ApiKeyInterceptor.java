package no.hvl.dat108.oblig4.keys;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.security.MessageDigest;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {
    private final String apiKey;

    public ApiKeyInterceptor(@Value("${app.api-key}") String apiKey) { this.apiKey = apiKey; }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
        String given = req.getHeader("X-API-KEY");
        if (given == null || !MessageDigest.isEqual(given.getBytes(), apiKey.getBytes())) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }
}


