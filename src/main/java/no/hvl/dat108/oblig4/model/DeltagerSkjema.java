package no.hvl.dat108.oblig4.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class DeltagerSkjema {

    public static final String STRING_START = "^";
    public static final String STRING_END = "$";
    public static final String A_LETTER_SPACE_OR_HYPHEN = "[a-zA-ZæøåÆØÅ -]";
    public static final String A_LETTER_OR_HYPHEN = "[a-zA-ZæøåÆØÅ-]";
    public static final String A_CAPITAL_LETTER = "[A-ZÆØÅ]";
    public static final String A_DIGIT = "[0-9]";
    public static final String ZERO_OR_MORE_TIMES = "*";
    public static final String ONE_TO_19_TIMES = "{1,19}";
    public static final String EIGHT_TIMES = "{8}";

    @NotNull(message = Feilmeldinger.UGYLDIG_FORNAVN_MLD)
    @Pattern(regexp = STRING_START + A_CAPITAL_LETTER + A_LETTER_SPACE_OR_HYPHEN
                    + ONE_TO_19_TIMES + STRING_END,
             message = Feilmeldinger.UGYLDIG_FORNAVN_MLD)
	private String fornavn;

    @NotNull(message = Feilmeldinger.UGYLDIG_ETTERNAVN_MLD)
    @Pattern(regexp = STRING_START + A_CAPITAL_LETTER + A_LETTER_OR_HYPHEN
                    + ONE_TO_19_TIMES + STRING_END,
             message = Feilmeldinger.UGYLDIG_ETTERNAVN_MLD)
	private String etternavn;

    @NotNull(message = Feilmeldinger.UGYLDIG_MOBIL_MLD)
    @Pattern(regexp = STRING_START + A_DIGIT + EIGHT_TIMES + STRING_END,
             message = Feilmeldinger.UGYLDIG_MOBIL_MLD)
	private String mobil;

    @NotNull(message = Feilmeldinger.UGYLDIG_PASSORD_MLD)
    @Size(min = 4, message = Feilmeldinger.UGYLDIG_PASSORD_MLD)
	private String passord;

	private String passordRepetert;

    @NotNull(message = Feilmeldinger.UGYLDIG_KJONN_MLD)
    @Pattern(regexp = "^(mann|kvinne)$", message = Feilmeldinger.UGYLDIG_KJONN_MLD)
	private String kjonn;

    public Deltager lagDeltager(String hashetPassord) {
    	return new Deltager(mobil, hashetPassord, fornavn, etternavn, kjonn);
 	}
	
	public String getFornavn() {
		return fornavn;
	}
	public void setFornavn(String fornavn) {
		this.fornavn = fornavn;
	}
	public String getEtternavn() {
		return etternavn;
	}
	public void setEtternavn(String etternavn) {
		this.etternavn = etternavn;
	}
	public String getMobil() {
		return mobil;
	}
	public void setMobil(String mobil) {
		this.mobil = mobil;
	}
	public String getPassord() {
		return passord;
	}
	public void setPassord(String passord) {
		this.passord = passord;
	}
	public String getPassordRepetert() {
		return passordRepetert;
	}
	public void setPassordRepetert(String passordRepetert) {
		this.passordRepetert = passordRepetert;
	}
	public String getKjonn() {
		return kjonn;
	}
	public void setKjonn(String kjonn) {
		this.kjonn = kjonn;
	}

}
