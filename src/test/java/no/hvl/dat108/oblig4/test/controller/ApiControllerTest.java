package no.hvl.dat108.oblig4.test.controller;

import no.hvl.dat108.oblig4.keys.ApiKeyInterceptor;
import no.hvl.dat108.oblig4.model.Deltager;
import no.hvl.dat108.oblig4.service.ApiNokkelService;
import no.hvl.dat108.oblig4.service.DeltagerDbService;
import no.hvl.dat108.oblig4.service.PassordService;
import no.hvl.dat108.oblig4.controller.ApiController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Standalone MockMvc-test av alle 9 mulige responser fra API-et.
 * Ingen database og ingen Spring-kontekst: services mockes, og
 * API-nøkkel-interceptoren kobles på manuelt.
 */
class ApiControllerTest {

    private static final String NOKKEL = "test-nokkel";
    private static final String HEADER = "Api-Key";

    private static final String GYLDIG_JSON = """
            {"fornavn":"Lise","etternavn":"Hansen","mobil":"11223344",
             "passord":"hemmelig","kjonn":"kvinne"}""";

    private DeltagerDbService db;
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        db = mock(DeltagerDbService.class);
        mvc = MockMvcBuilders
                .standaloneSetup(new ApiController(db, new PassordService()))
                .addInterceptors(new ApiKeyInterceptor(new ApiNokkelService(NOKKEL)))
                .build();
    }

    // ---------- GET /api/deltagere ----------

    @Test
    void hentAlle_200_utenPassordhash() throws Exception {
        when(db.finnAlleSortertPaaFulltNavn()).thenReturn(
                List.of(new Deltager("11111111", "GEHEIMHASH", "Ola", "Nordmann", "mann")));

        String body = mvc.perform(get("/api/deltagere").header(HEADER, NOKKEL))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertTrue(body.contains("\"mobil\":\"11111111\""));
        assertFalse(body.contains("GEHEIMHASH"));
        assertFalse(body.toLowerCase().contains("passord"));
    }

    @Test
    void hentAlle_401_utenNokkel_medMelding() throws Exception {
        String body = mvc.perform(get("/api/deltagere"))
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse().getContentAsString();

        assertFalse(body.isBlank());
    }

    // ---------- GET /api/deltagere/{mobil} ----------

    @Test
    void hentEn_200() throws Exception {
        when(db.finnDeltagerMedMobil("11111111"))
                .thenReturn(new Deltager("11111111", "hash", "Ola", "Nordmann", "mann"));

        mvc.perform(get("/api/deltagere/11111111").header(HEADER, NOKKEL))
                .andExpect(status().isOk());
    }

    @Test
    void hentEn_401_feilNokkel() throws Exception {
        mvc.perform(get("/api/deltagere/11111111").header(HEADER, "feil"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void hentEn_404() throws Exception {
        when(db.finnDeltagerMedMobil("00000000")).thenReturn(null);

        mvc.perform(get("/api/deltagere/00000000").header(HEADER, NOKKEL))
                .andExpect(status().isNotFound());
    }

    // ---------- POST /api/deltagere ----------

    @Test
    void post_201_medLocationOgHashetPassord() throws Exception {
        when(db.erDeltagerPaameldt("11223344")).thenReturn(false);

        MockHttpServletResponse res = mvc.perform(post("/api/deltagere")
                        .header(HEADER, NOKKEL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(GYLDIG_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse();

        assertNotNull(res.getHeader("Location"));
        assertTrue(res.getHeader("Location").endsWith("/api/deltagere/11223344"));
        assertFalse(res.getContentAsString().isBlank());

        ArgumentCaptor<Deltager> captor = ArgumentCaptor.forClass(Deltager.class);
        verify(db).lagreDeltager(captor.capture());
        assertEquals("11223344", captor.getValue().getMobil());
        assertNotEquals("hemmelig", captor.getValue().getHashetPassord());
    }

    @Test
    void post_401_utenNokkel() throws Exception {
        mvc.perform(post("/api/deltagere")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(GYLDIG_JSON))
                .andExpect(status().isUnauthorized());

        verify(db, never()).lagreDeltager(any());
    }

    @Test
    void post_401_selvOmBodyOgsaaErUgyldig() throws Exception {
        mvc.perform(post("/api/deltagere")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(GYLDIG_JSON.replace("11223344", "123")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void post_400_ugyldigMobil() throws Exception {
        mvc.perform(post("/api/deltagere")
                        .header(HEADER, NOKKEL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(GYLDIG_JSON.replace("11223344", "123")))
                .andExpect(status().isBadRequest());

        verify(db, never()).lagreDeltager(any());
    }

    @Test
    void post_400_manglendeFelt() throws Exception {
        mvc.perform(post("/api/deltagere")
                        .header(HEADER, NOKKEL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verify(db, never()).lagreDeltager(any());
    }

    @Test
    void post_409_alleredePaameldt() throws Exception {
        when(db.erDeltagerPaameldt("11223344")).thenReturn(true);

        String body = mvc.perform(post("/api/deltagere")
                        .header(HEADER, NOKKEL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(GYLDIG_JSON))
                .andExpect(status().isConflict())
                .andReturn().getResponse().getContentAsString();

        assertFalse(body.isBlank());
        verify(db, never()).lagreDeltager(any());
    }
}
