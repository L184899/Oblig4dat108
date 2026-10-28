package no.hvl.dat108.oblig4.service;

import no.hvl.dat108.oblig4.model.Deltager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeltagerDbService {

    private static List<Deltager> deltagerliste = new ArrayList<>(List.of(
        new Deltager("12345678", "dummyhash", "Knut", "Knutsen", "mann"),
        new Deltager("23456789", "dummyhash", "Knut", "Olsen", "mann"),
        new Deltager("34567890", "dummyhash", "Anne", "Annesen", "kvinne"),
        new Deltager("11223344", "dummyhash", "Lise", "Hansen", "kvinne")
    ));

    public static Deltager finnDeltagerMedMobil(String mobil) {

        return deltagerliste.stream()
                .filter(d -> d.getMobil().equals(mobil))
                .findFirst()
                .orElse(null);
    }

    public static List<Deltager> finnAlleSortertPaaFulltNavn() {

        return deltagerliste.stream()
                .sorted(Deltager.paaFulltNavn)
                .toList();
    }

    public static boolean erDeltagerPaameldt(String mobil) {

        return finnDeltagerMedMobil(mobil) != null;
    }

    public static void lagreDeltager(Deltager deltager) {

        deltagerliste.add(deltager);
    }
}
