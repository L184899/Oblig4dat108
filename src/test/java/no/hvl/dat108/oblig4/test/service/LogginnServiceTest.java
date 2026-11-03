package no.hvl.dat108.oblig4.test.service;

import no.hvl.dat108.oblig4.model.Deltager;
import no.hvl.dat108.oblig4.service.LogginnService;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import static org.junit.jupiter.api.Assertions.*;


class LogginnServiceTest {

    private final LogginnService service = new LogginnService();
    private final Deltager deltager = new Deltager("12345678", "hash", "Ola", "Nordmann", "mann");

    @Test
    void ikkeInnloggetFoerInnlogging() {
        assertFalse(service.erBrukerLoggetInn(new MockHttpServletRequest()));
    }

    @Test
    void innloggetEtterInnlogging() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        service.loggInnBruker(request, deltager);

        assertTrue(service.erBrukerLoggetInn(request));
        assertEquals(deltager, service.hentInnloggetBruker(request));
    }

    @Test
    void ikkeInnloggetEtterUtlogging() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        service.loggInnBruker(request, deltager);

        service.loggUtBruker(request);

        assertFalse(service.erBrukerLoggetInn(request));
    }
}
