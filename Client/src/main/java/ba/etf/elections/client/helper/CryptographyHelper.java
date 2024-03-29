/*
 * Copyright (c) 2023. Vahidin Hasić
 */

package ba.etf.elections.client.helper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class CryptographyHelper {
    /**
     * Creates MAC hash for the vote
     * <a href="https://www.baeldung.com/java-hmac">Link</a>
     *
     * @param data Data to be hashed
     * @return MAC hash
     */
    public static String createMACHash(String data) throws NoSuchAlgorithmException, InvalidKeyException {
        String key = CommonFunctions.getEnvironmentVariable("key");
        String algorithm = "HmacSHA256";
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), algorithm);
        Mac mac = Mac.getInstance(algorithm);
        mac.init(secretKeySpec);
        return bytesToHex(mac.doFinal(data.getBytes()));
    }

    /**
     * Validates MAC hash for the vote
     *
     * @param data            Data to be hashed
     * @param hmacSHA256Value MAC hash to be validated
     * @return True if MAC hash is valid, false otherwise
     */
    public static Boolean validateMACHash(String data, String hmacSHA256Value) throws NoSuchAlgorithmException, InvalidKeyException {
        String macHash = createMACHash(data);
        return macHash.equals(hmacSHA256Value);
    }

    /**
     * Converts mac bytes to characters
     *
     * @param bytes Bytes to be converted to hex
     * @return Hex representation of the bytes
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
