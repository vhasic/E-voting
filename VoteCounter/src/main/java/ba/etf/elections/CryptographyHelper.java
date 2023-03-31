/*
 * Copyright (c) 2023. Vahidin Hasić
 */

package ba.etf.elections;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class CryptographyHelper {
    /**
     * Reads the environment variable with the given name or if it doesn't exist, reads the system property with the given name
     *
     * @param name name of the environment variable or system property
     * @return value of the environment variable or system property
     */
    public static String getEnvironmentVariable(String name) {
        String value = System.getenv(name); // this gets environment variable "systemPassword" set like this: export systemPassword=<password_value>
        if (value == null) {
            value = System.getProperty(name); // this gets system property "systemPassword" set like this: java -jar Client.jar -DsystemPassword=<password_value>
        }
        return value;
    }

    /**
     * Creates MAC hash for the vote
     * <a href="https://www.baeldung.com/java-hmac">Link</a>
     *
     * @param data Data to be hashed
     * @return MAC hash
     */
    public static String createMACHash(String data) throws NoSuchAlgorithmException, InvalidKeyException {
        String key = getEnvironmentVariable("key");
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
