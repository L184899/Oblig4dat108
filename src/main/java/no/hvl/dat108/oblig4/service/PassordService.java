package no.hvl.dat108.oblig4.service;

import jakarta.xml.bind.DatatypeConverter;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class PassordService {

	private final static String DEFAULT_SALT = "ABCD";

    public static String lagHashetPassord(String passordKlartekst) {
        return hashMedSalt(passordKlartekst, DEFAULT_SALT);
    }

	public static boolean erPassordKorrekt(
			String passord, String hashetPassord) {
		
		if (passord == null || hashetPassord == null) { //Burde validert skikkelig!!
			throw new IllegalArgumentException();
		}
		return hashetPassord.equals(lagHashetPassord(passord));
	}

    private static String genererTilfeldigSalt() {
        SecureRandom sr;
        byte[] salt = new byte[16];
        try {
            sr = SecureRandom.getInstance("SHA1PRNG");
            sr.nextBytes(salt);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return DatatypeConverter.printHexBinary(salt);
    }


    private static String hashMedSalt(String passord, String salt) {

        if (passord == null || salt == null) { //Burde validert skikkelig!!
            throw new IllegalArgumentException();
        }

        char[] passchar = passord.toCharArray();
        byte[] saltbytes = DatatypeConverter.parseHexBinary(salt);

        byte[] keyhash = null;
        try {
            PBEKeySpec pks = new PBEKeySpec(passchar, saltbytes, 1000, 256);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            keyhash = skf.generateSecret(pks).getEncoded();

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return DatatypeConverter.printHexBinary(keyhash);
    }

}
