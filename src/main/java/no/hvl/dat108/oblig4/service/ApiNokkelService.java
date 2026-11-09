package no.hvl.dat108.oblig4.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ApiNokkelService {

    private final String denEnesteGyldigeApiNokkelen;

    public ApiNokkelService(@Value("${app.api-key}") String nokkel) {
        this.denEnesteGyldigeApiNokkelen = nokkel;
    }

    public boolean erNokkelGyldig(String nokkel) {
        return denEnesteGyldigeApiNokkelen.equals(nokkel);
    }
}
