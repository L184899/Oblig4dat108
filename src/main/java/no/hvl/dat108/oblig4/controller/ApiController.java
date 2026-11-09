package no.hvl.dat108.oblig4.controller;

import jakarta.validation.Valid;
import no.hvl.dat108.oblig4.model.Deltager;
import no.hvl.dat108.oblig4.model.DeltagerPostRequestDTO;
import no.hvl.dat108.oblig4.model.DeltagerResponseDTO;
import no.hvl.dat108.oblig4.model.Feilmeldinger;
import no.hvl.dat108.oblig4.service.DeltagerDbService;
import no.hvl.dat108.oblig4.service.PassordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/deltagere")
public class ApiController {

    private final DeltagerDbService deltagerService;
    private final PassordService passordService;

    public ApiController(DeltagerDbService ds, PassordService ps) {
        this.deltagerService = ds;
        this.passordService = ps;
    }

    @GetMapping
    public List<DeltagerResponseDTO> hentAlle() {
        return deltagerService.finnAlleSortertPaaFulltNavn().stream()
                .map(DeltagerResponseDTO::new)
                .toList();
    }

    @GetMapping("/{mobil}")
    public ResponseEntity<DeltagerResponseDTO> hentEn(@PathVariable String mobil) {
        Deltager d = deltagerService.finnDeltagerMedMobil(mobil);
        if (d == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new DeltagerResponseDTO(d));
    }

    @PostMapping
    public ResponseEntity<String> meldPaa(@Valid @RequestBody DeltagerPostRequestDTO dto) {

        if (deltagerService.erDeltagerPaameldt(dto.mobil())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Feilmeldinger.ALLEREDE_PAAMELDT_MLD);
        }

        Deltager deltager = dto.lagDeltager(passordService.lagHashetPassord(dto.passord()));
        deltagerService.lagreDeltager(deltager);

        URI location = URI.create("/api/deltagere/" + deltager.getMobil());

        return ResponseEntity.created(location).body(Feilmeldinger.API_PAAMELDT_OK_MLD);
    }
}
