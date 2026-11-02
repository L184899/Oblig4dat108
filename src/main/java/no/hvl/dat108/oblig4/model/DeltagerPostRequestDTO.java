package no.hvl.dat108.oblig4.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import static no.hvl.dat108.oblig4.model.DeltagerSkjema.*;

/** Data som sendes inn i POST /api/deltagere. Samme valideringsregler som i Oblig4. */
public record DeltagerPostRequestDTO(

        @NotNull(message = Feilmeldinger.UGYLDIG_MOBIL_MLD)
        @Pattern(regexp = STRING_START + A_DIGIT + EIGHT_TIMES + STRING_END,
                message = Feilmeldinger.UGYLDIG_MOBIL_MLD)
        String mobil,

        @NotNull(message = Feilmeldinger.UGYLDIG_PASSORD_MLD)
        @Size(min = 4, message = Feilmeldinger.UGYLDIG_PASSORD_MLD)
        String passord, // passord i klartekst

        @NotNull(message = Feilmeldinger.UGYLDIG_FORNAVN_MLD)
        @Pattern(regexp = STRING_START + A_CAPITAL_LETTER + A_LETTER_SPACE_OR_HYPHEN
                + ONE_TO_19_TIMES + STRING_END,
                message = Feilmeldinger.UGYLDIG_FORNAVN_MLD)
        String fornavn,

        @NotNull(message = Feilmeldinger.UGYLDIG_ETTERNAVN_MLD)
        @Pattern(regexp = STRING_START + A_CAPITAL_LETTER + A_LETTER_OR_HYPHEN
                + ONE_TO_19_TIMES + STRING_END,
                message = Feilmeldinger.UGYLDIG_ETTERNAVN_MLD)
        String etternavn,

        @NotNull(message = Feilmeldinger.UGYLDIG_KJONN_MLD)
        @Pattern(regexp = "^(mann|kvinne)$", message = Feilmeldinger.UGYLDIG_KJONN_MLD)
        String kjonn) {

    public Deltager lagDeltager(String hashetPassord) {
        return new Deltager(mobil, hashetPassord, fornavn, etternavn, kjonn);
    }
}
