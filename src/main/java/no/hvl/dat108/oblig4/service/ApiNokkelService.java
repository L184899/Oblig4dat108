package no.hvl.dat108.oblig4.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Service
public class ApiNokkelService {

    private final String denEnesteGyldigeApiNokkelen;

    // Verdi hentes fra application.properties (app.api-key)
    public ApiNokkelService(@Value("${app.api-key}") String nokkel) {
        this.denEnesteGyldigeApiNokkelen = nokkel;
    }

    public boolean erNokkelGyldig(String nokkel) {
        return nokkel != null && MessageDigest.isEqual(
                nokkel.getBytes(StandardCharsets.UTF_8),
                denEnesteGyldigeApiNokkelen.getBytes(StandardCharsets.UTF_8));
    }
}
