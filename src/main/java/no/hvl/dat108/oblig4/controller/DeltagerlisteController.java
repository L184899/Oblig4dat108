package no.hvl.dat108.oblig4.controller;

import jakarta.servlet.http.HttpServletRequest;
import no.hvl.dat108.oblig4.model.Feilmeldinger;
import no.hvl.dat108.oblig4.service.DeltagerDbService;
import no.hvl.dat108.oblig4.service.LogginnService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DeltagerlisteController {

    private final DeltagerDbService deltagerService;
    private final LogginnService logginnService;

    public DeltagerlisteController(DeltagerDbService ds, LogginnService ls) {
        this.deltagerService = ds;
        this.logginnService = ls;
    }

    @GetMapping("/deltagerliste")
    public String henteDeltagerliste(HttpServletRequest request, Model model,
                                     RedirectAttributes ra) {

        if (!logginnService.erBrukerLoggetInn(request)) {
            ra.addFlashAttribute("feilmelding", Feilmeldinger.KREVER_INNLOGGET_MLD);
            return "redirect:/innlogging";
        }

        model.addAttribute("innlogget", logginnService.hentInnloggetBruker(request));
        model.addAttribute("deltagere", deltagerService.finnAlleSortertPaaFulltNavn());
        return "deltagerliste";
    }

    // Kalles av JavaScript hvert 5. sekund. Krever innlogging (session), ikke API-nøkkel.
    @GetMapping("/deltagerliste/antall")
    @ResponseBody
    public ResponseEntity<Long> antall(HttpServletRequest request) {
        if (!logginnService.erBrukerLoggetInn(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(deltagerService.antallDeltagere());
    }
}