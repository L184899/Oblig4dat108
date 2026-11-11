package no.hvl.dat108.oblig4.service;

import no.hvl.dat108.oblig4.model.Deltager;
import no.hvl.dat108.oblig4.repository.DeltagerRepository;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;

@Service
public class DeltagerDbService {

    private final DeltagerRepository deltagerRepository;

    public DeltagerDbService(DeltagerRepository deltagerRepository) {
        this.deltagerRepository = deltagerRepository;
    }

    public Deltager finnDeltagerMedMobil(String mobil) {
        return deltagerRepository.findById(mobil).orElse(null);
    }

    public List<Deltager> finnAlleSortertPaaFulltNavn() {
        return deltagerRepository.findAll().stream()
                .sorted(Comparator.comparing(Deltager::getFornavn)
                        .thenComparing(Deltager::getEtternavn))
                .toList();
    }

    public boolean erDeltagerPaameldt(String mobil) {
        return deltagerRepository.existsById(mobil);
    }

    public void lagreDeltager(Deltager deltager) {
        deltagerRepository.save(deltager);
    }
}