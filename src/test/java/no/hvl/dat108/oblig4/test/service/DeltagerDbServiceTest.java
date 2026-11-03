package no.hvl.dat108.oblig4.test.service;

import no.hvl.dat108.oblig4.model.Deltager;
import no.hvl.dat108.oblig4.repository.DeltagerRepository;
import no.hvl.dat108.oblig4.service.DeltagerDbService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeltagerDbServiceTest {

    private DeltagerRepository repo;
    private DeltagerDbService service;

    @BeforeEach
    void setUp() {
        repo = mock(DeltagerRepository.class);
        service = new DeltagerDbService(repo);
    }

    @Test
    void finnDeltagerMedMobilGirDeltagerNaarDenFinnes() {
        Deltager d = new Deltager("12345678", "hash", "Ola", "Nordmann", "mann");
        when(repo.findById("12345678")).thenReturn(Optional.of(d));

        assertSame(d, service.finnDeltagerMedMobil("12345678"));
    }

    @Test
    void finnDeltagerMedMobilGirNullNaarDenIkkeFinnes() {
        when(repo.findById("00000000")).thenReturn(Optional.empty());

        assertNull(service.finnDeltagerMedMobil("00000000"));
    }

    @Test
    void erDeltagerPaameldtDelegererTilRepository() {
        when(repo.existsById("12345678")).thenReturn(true);

        assertTrue(service.erDeltagerPaameldt("12345678"));
        assertFalse(service.erDeltagerPaameldt("99999999"));
    }

    @Test
    void lagreDeltagerKallerSave() {
        Deltager d = new Deltager("12345678", "hash", "Ola", "Nordmann", "mann");

        service.lagreDeltager(d);

        verify(repo).save(d);
    }

    @Test
    void finnAlleBrukerSortertSpoerring() {
        List<Deltager> liste = List.of(new Deltager("11111111", "h", "Anne", "Panne", "kvinne"));
        when(repo.findAllByOrderByFornavnAscEtternavnAsc()).thenReturn(liste);

        assertEquals(liste, service.finnAlleSortertPaaFulltNavn());
    }
}
