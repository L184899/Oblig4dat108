package no.hvl.dat108.oblig4.test.service;

import org.junit.jupiter.api.Test;

class ApiNokkelServiceTest {

    private final ApiNokkelService service = new ApiNokkelService("topsecret");

    @Test
    void riktigNokkelErGyldig() {
        assertTrue(service.erNokkelGyldig("topsecret"));
    }

    @Test
    void feilNokkelErUgyldig() {
        assertFalse(service.erNokkelGyldig("feil"));
    }

    @Test
    void manglendeNokkelErUgyldig() {
        assertFalse(service.erNokkelGyldig(null));
    }
}
