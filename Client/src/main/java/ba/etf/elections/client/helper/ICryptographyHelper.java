package ba.etf.elections.client.helper;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface ICryptographyHelper {
    /**
     * Creates MAC hash for the vote
     * <a href="https://www.baeldung.com/java-hmac">Link</a>
     *
     * @param data Data to be hashed
     * @return MAC hash
     */
    static String createMACHash(String data) throws NoSuchAlgorithmException, InvalidKeyException {
        return null;
    }

    /**
     * Validates MAC hash for the vote
     *
     * @param data            Data to be hashed
     * @param hmacSHA256Value MAC hash to be validated
     * @return True if MAC hash is valid, false otherwise
     */
    static Boolean validateMACHash(String data, String hmacSHA256Value) throws NoSuchAlgorithmException, InvalidKeyException {
        return null;
    }
}