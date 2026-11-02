package no.hvl.dat108.oblig4.model;

/** Data som returneres fra API-et. Aldri med passord/hash. */
public record DeltagerResponseDTO(String mobil, String fornavn, String etternavn, String kjonn) {

    public DeltagerResponseDTO(Deltager d) {
        this(d.getMobil(), d.getFornavn(), d.getEtternavn(), d.getKjonn());
    }
}
