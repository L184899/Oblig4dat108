package no.hvl.dat108.oblig4.model;

/** Data som returneres fra API-et. Aldri med passord/hash. */
public record DeltagerSvarDTO(String mobil, String fornavn, String etternavn, String kjonn) {

    public DeltagerSvarDTO(Deltager d) {
        this(d.getMobil(), d.getFornavn(), d.getEtternavn(), d.getKjonn());
    }
}
