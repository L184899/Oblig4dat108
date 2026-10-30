package no.hvl.dat108.oblig4.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import no.hvl.dat108.oblig4.model.Deltager;
import no.hvl.dat108.oblig4.model.DeltagerSkjema;
import no.hvl.dat108.oblig4.model.Feilmeldinger;
import no.hvl.dat108.oblig4.service.DeltagerDbService;
import no.hvl.dat108.oblig4.service.LogginnService;
import no.hvl.dat108.oblig4.service.PassordService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PaameldingController {

    // NYTT: Dependency Injection via konstruktør (ingen static-kall lenger)
    private final DeltagerDbService deltagerService;
    private final LogginnService logginnService;
    private final PassordService passordService;

    public PaameldingController(DeltagerDbService ds, LogginnService ls, PassordService ps) {
        this.deltagerService = ds;
        this.logginnService = ls;
        this.passordService = ps;
    }

    @GetMapping("/paamelding")
    public String henteSkjema() {
        return "paameldingsskjema";
    }

    @PostMapping("/paamelding")
    public String meldePaa(HttpServletRequest request,
                           @Valid @ModelAttribute("deltager") DeltagerSkjema skjema,
                           BindingResult bindingResult, Model model) {

        String feilmelding = meldingFraInputValidering(skjema, bindingResult);

        if (feilmelding != null) {
            model.addAttribute("feilmelding", feilmelding);
            return "paameldingsskjema";
        }

        String hashetPassord = passordService.lagHashetPassord(skjema.getPassord());
        Deltager deltager = skjema.lagDeltager(hashetPassord);

        deltagerService.lagreDeltager(deltager);
        logginnService.loggInnBruker(request, deltager);

        return "redirect:/paameldt";
    }

    @GetMapping("/paameldt")
    public String hentePaameldt(HttpServletRequest request, RedirectAttributes ra,
                                Model model) {

        if (!logginnService.erBrukerLoggetInn(request)) {
            ra.addFlashAttribute("feilmelding", Feilmeldinger.KREVER_INNLOGGET_MLD);
            return "redirect:/innlogging";
        }

        model.addAttribute("deltager", logginnService.hentInnloggetBruker(request));
        return "paameldingsbekreftelse";
    }

    private String meldingFraInputValidering(DeltagerSkjema skjema, BindingResult bindingResult) {
        String melding = null;
        if (bindingResult.hasErrors()) {
            melding = Feilmeldinger.GENERELL_BINDING_MLD;

        } else if (!skjema.getPassord().equals(skjema.getPassordRepetert())) {
            melding = Feilmeldinger.FEIL_REPETERT_PASSORD_MLD;

        } else if (deltagerService.erDeltagerPaameldt(skjema.getMobil())) {
            melding = Feilmeldinger.ALLEREDE_PAAMELDT_MLD;
        }
        return melding;
    }
}