package no.hvl.dat108.oblig4.keys;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import no.hvl.dat108.oblig4.model.Feilmeldinger;
import no.hvl.dat108.oblig4.service.ApiNokkelService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    public static final String HEADER = "Api-Key";

    private final ApiNokkelService apiNokkelService;

    public ApiKeyInterceptor(ApiNokkelService apiNokkelService) {
        this.apiNokkelService = apiNokkelService;
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler)
            throws Exception {

        if (!apiNokkelService.erNokkelGyldig(req.getHeader(HEADER))) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("text/plain;charset=UTF-8");
            res.getWriter().write(Feilmeldinger.UGYLDIG_API_NOKKEL_MLD);
            return false;
        }
        return true;
    }
}
