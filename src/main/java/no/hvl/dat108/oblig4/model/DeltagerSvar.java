package no.hvl.dat108.oblig4.model;

public record DeltagerSvar(String mobil, String fornavn, String etternavn, String kjonn) {

    // Gjør om en Deltager (fra databasen) til et svar-objekt uten passord
    public static DeltagerSvar fra(Deltager d) {
        return new DeltagerSvar(d.getMobil(), d.getFornavn(), d.getEtternavn(), d.getKjonn());
    }
}