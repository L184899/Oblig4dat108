package no.hvl.dat108.oblig4.test.service;

import org.junit.jupiter.api.Test;
import no.hvl.dat108.oblig4.service.PassordService;
import static org.junit.jupiter.api.Assertions.*;

class PassordServiceTest {

    private final PassordService passordService = new PassordService();

    @Test
    void riktigPassordGodkjennes() {
        String hash = passordService.lagHashetPassord("hemmelig");
        assertTrue(passordService.erPassordKorrekt("hemmelig", hash));
    }

    @Test
    void feilPassordAvvises() {
        String hash = passordService.lagHashetPassord("hemmelig");
        assertFalse(passordService.erPassordKorrekt("feil", hash));
    }
}
