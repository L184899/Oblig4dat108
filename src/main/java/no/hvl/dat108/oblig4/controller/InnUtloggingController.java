package no.hvl.dat108.oblig4.controller;

import jakarta.servlet.http.HttpServletRequest;
import no.hvl.dat108.oblig4.model.Deltager;
import no.hvl.dat108.oblig4.model.Feilmeldinger;
import no.hvl.dat108.oblig4.service.DeltagerDbService;
import no.hvl.dat108.oblig4.service.LogginnService;
import no.hvl.dat108.oblig4.service.PassordService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class InnUtloggingController {

    private final DeltagerDbService deltagerService;
    private final LogginnService logginnService;
    private final PassordService passordService;

    public InnUtloggingController(DeltagerDbService ds, LogginnService ls, PassordService ps) {
        this.deltagerService = ds;
        this.logginnService = ls;
        this.passordService = ps;
    }

    @GetMapping("/innlogging")
    public String henteSkjema() {
        return "logginn";
    }

    @PostMapping("/innlogging")
    public String loggeInn(@RequestParam String mobil, @RequestParam String passord,
                           HttpServletRequest request, RedirectAttributes ra) {

        // ENDRET: instansmetoder via injiserte services (var static-kall i Oblig4)
        Deltager deltager = deltagerService.finnDeltagerMedMobil(mobil);

        if (deltager == null
                || !passordService.erPassordKorrekt(passord, deltager.getHashetPassord())) {
            ra.addFlashAttribute("feilmelding", Feilmeldinger.UGYLDIG_BRUKERNAVN_PASSORD_MLD);
            return "redirect:/innlogging";
        }

        logginnService.loggInnBruker(request, deltager);
        return "redirect:/deltagerliste";
    }

    @PostMapping("/utlogging")
    public String loggeUt(HttpServletRequest request, RedirectAttributes ra) {

        logginnService.loggUtBruker(request);
        ra.addFlashAttribute("feilmelding", Feilmeldinger.UTLOGGET_MLD);
        return "redirect:/innlogging";
    }
}
