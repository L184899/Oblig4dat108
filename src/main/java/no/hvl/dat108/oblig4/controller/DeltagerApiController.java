package no.hvl.dat108.oblig4.controller;

import jakarta.validation.Valid;
import no.hvl.dat108.oblig4.model.Deltager;
import no.hvl.dat108.oblig4.model.DeltagerSkjema;
import no.hvl.dat108.oblig4.model.DeltagerSvar;
import no.hvl.dat108.oblig4.model.Feilmeldinger;
import no.hvl.dat108.oblig4.service.DeltagerDbService;
import no.hvl.dat108.oblig4.service.PassordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deltagere")
public class DeltagerApiController {

    private final DeltagerDbService deltagerService;
    private final PassordService passordService;

    public DeltagerApiController(DeltagerDbService ds, PassordService ps) {
        this.deltagerService = ds;
        this.passordService = ps;
    }

    // GET /api/deltagere : alle deltagere
    @GetMapping
    public List<DeltagerSvar> hentAlle() {
        return deltagerService.finnAlleSortertPaaFulltNavn().stream()
                .map(DeltagerSvar::fra)
                .toList();
    }

    // GET /api/deltagere/{mobil} : én deltager, eller 404
    @GetMapping("/{mobil}")
    public ResponseEntity<DeltagerSvar> hentEn(@PathVariable String mobil) {
        Deltager d = deltagerService.finnDeltagerMedMobil(mobil);
        if (d == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(DeltagerSvar.fra(d));
    }

    // POST /api/deltagere : meld på ny deltager
    @PostMapping
    public ResponseEntity<?> meldPaa(@Valid @RequestBody DeltagerSkjema skjema,
                                     BindingResult bindingResult) {

        if (bindingResult.hasErrors()
                || !skjema.getPassord().equals(skjema.getPassordRepetert())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Feilmeldinger.GENERELL_BINDING_MLD);
        }
        if (deltagerService.erDeltagerPaameldt(skjema.getMobil())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Feilmeldinger.ALLEREDE_PAAMELDT_MLD);
        }

        Deltager deltager = skjema.lagDeltager(
                passordService.lagHashetPassord(skjema.getPassord()));
        deltagerService.lagreDeltager(deltager);

        return ResponseEntity.status(HttpStatus.CREATED).body(DeltagerSvar.fra(deltager));
    }
}