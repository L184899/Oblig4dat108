package no.hvl.dat108.oblig4.test.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import no.hvl.dat108.oblig4.model.DeltagerSkjema;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeltagerSkjemaValideringTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private DeltagerSkjema gyldig() {
        DeltagerSkjema s = new DeltagerSkjema();
        s.setFornavn("Ola");
        s.setEtternavn("Nordmann");
        s.setMobil("12345678");
        s.setPassord("hemmelig");
        s.setPassordRepetert("hemmelig");
        s.setKjonn("mann");
        return s;
    }

    private boolean erGyldig(DeltagerSkjema s) {
        return validator.validate(s).isEmpty();
    }

    @Test
    void gyldigSkjemaGodkjennes() {
        assertTrue(erGyldig(gyldig()));
    }

    @Test
    void fornavnMaaStarteMedStorBokstav() {
        DeltagerSkjema s = gyldig();
        s.setFornavn("ola");
        assertFalse(erGyldig(s));
    }

    @Test
    void fornavnKanHaMellomromOgBindestrekOgNorskeTegn() {
        DeltagerSkjema s = gyldig();
        s.setFornavn("Åse Marie-Øyvor");
        assertTrue(erGyldig(s));
    }

    @Test
    void fornavnForKortOgForLangAvvises() {
        DeltagerSkjema s = gyldig();
        s.setFornavn("O");
        assertFalse(erGyldig(s));
        s.setFornavn("A" + "a".repeat(20)); // 21 tegn
        assertFalse(erGyldig(s));
        s.setFornavn("A" + "a".repeat(19)); // 20 tegn
        assertTrue(erGyldig(s));
    }

    @Test
    void etternavnKanIkkeHaMellomrom() {
        DeltagerSkjema s = gyldig();
        s.setEtternavn("Nor mann");
        assertFalse(erGyldig(s));
    }

    @Test
    void etternavnKanHaBindestrek() {
        DeltagerSkjema s = gyldig();
        s.setEtternavn("Hansen-Berg");
        assertTrue(erGyldig(s));
    }

    @Test
    void mobilMaaVaereNoyaktigAttSiffer() {
        DeltagerSkjema s = gyldig();
        s.setMobil("1234567");
        assertFalse(erGyldig(s));
        s.setMobil("123456789");
        assertFalse(erGyldig(s));
        s.setMobil("1234abcd");
        assertFalse(erGyldig(s));
    }

    @Test
    void passordForKortAvvises() {
        DeltagerSkjema s = gyldig();
        s.setPassord("abc");
        assertFalse(erGyldig(s));
    }

    @Test
    void kjonnMaaVaereMannEllerKvinne() {
        DeltagerSkjema s = gyldig();
        s.setKjonn("annet");
        assertFalse(erGyldig(s));
        s.setKjonn("kvinne");
        assertTrue(erGyldig(s));
    }

    @Test
    void manglendeFeltAvvises() {
        assertFalse(erGyldig(new DeltagerSkjema()));
    }
}
