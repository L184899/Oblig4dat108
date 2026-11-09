package no.hvl.dat108.oblig4.test.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import no.hvl.dat108.oblig4.model.Deltager;
import no.hvl.dat108.oblig4.model.DeltagerPostRequestDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DeltagerPostRequestDTOValideringTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private boolean erGyldig(String mobil, String passord, String fornavn,
                             String etternavn, String kjonn) {
        return validator.validate(
                new DeltagerPostRequestDTO(mobil, passord, fornavn, etternavn, kjonn)).isEmpty();
    }

    @Test
    void gyldigDtoGodkjennes() {
        Assertions.assertTrue(erGyldig("12345678", "hemmelig", "Ola", "Nordmann", "mann"));
    }

    @Test
    void ugyldigMobilAvvises() {
        Assertions.assertFalse(erGyldig("123", "hemmelig", "Ola", "Nordmann", "mann"));
        Assertions.assertFalse(erGyldig("1234567a", "hemmelig", "Ola", "Nordmann", "mann"));
        Assertions.assertFalse(erGyldig(null, "hemmelig", "Ola", "Nordmann", "mann"));
    }

    @Test
    void ugyldigFornavnOgEtternavnAvvises() {
        Assertions.assertFalse(erGyldig("12345678", "hemmelig", "ola", "Nordmann", "mann"));
        Assertions.assertFalse(erGyldig("12345678", "hemmelig", "Ola", "Nor mann", "mann"));
        Assertions.assertFalse(erGyldig("12345678", "hemmelig", "O", "Nordmann", "mann"));
    }

    @Test
    void forKortPassordAvvises() {
        Assertions.assertFalse(erGyldig("12345678", "abc", "Ola", "Nordmann", "mann"));
    }

    @Test
    void ugyldigKjonnAvvises() {
        Assertions.assertFalse(erGyldig("12345678", "hemmelig", "Ola", "Nordmann", "annet"));
    }

    @Test
    void lagDeltagerBrukerHashetPassord() {
        DeltagerPostRequestDTO dto =
                new DeltagerPostRequestDTO("12345678", "hemmelig", "Ola", "Nordmann", "mann");

        Deltager d = dto.lagDeltager("HASH");

        Assertions.assertEquals("HASH", d.getHashetPassord());
        Assertions.assertEquals("12345678", d.getMobil());
    }
}
