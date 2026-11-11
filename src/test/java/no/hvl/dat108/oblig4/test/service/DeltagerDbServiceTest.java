package no.hvl.dat108.oblig4.test.service;

import no.hvl.dat108.oblig4.model.Deltager;
import no.hvl.dat108.oblig4.repository.DeltagerRepository;
import no.hvl.dat108.oblig4.service.DeltagerDbService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.List;

class DeltagerDbServiceTest {

    private DeltagerRepository repository;
    private DeltagerDbService service;

    private final Deltager Liza = new Deltager("11111111", "hash", "Liza", "Lah", "kvinne");
    private final Deltager Marina = new Deltager("22222222", "hash", "Marina", "Ful", "kvinne");
    private final Deltager Lasse = new Deltager("33333333", "hash", "Lasse", "Aas", "mann");

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(DeltagerRepository.class);
        service = new DeltagerDbService(repository);
        Mockito.when(repository.findAll()).thenReturn(List.of(Liza, Marina, Lasse));
    }

    @Test
    void returnererAlleDeltagerneFraRepositoriet() {
        List<Deltager> resultat = service.finnAlleSortertPaaFulltNavn();

        Assertions.assertEquals(3, resultat.size());
        Assertions.assertTrue(resultat.contains(Liza));
        Assertions.assertTrue(resultat.contains(Marina));
        Assertions.assertTrue(resultat.contains(Lasse));
    }

    @Test
    void deltagerneErSortertPaaFornavnSaaEtternavn() {
        List<Deltager> resultat = service.finnAlleSortertPaaFulltNavn();

        Assertions.assertEquals(List.of(Lasse, Liza, Marina), resultat);
    }
}