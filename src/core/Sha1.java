package core;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha1 {

    public static String hash(String cleartext) {

        try {
            MessageDigest hash = MessageDigest.getInstance("SHA-1");
            byte[] msgBytes = cleartext.getBytes();
            byte[] sha1 = hash.digest(msgBytes);

            return bytesToHex(sha1);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String bytesToHex(byte[] data) {
        StringBuilder results = new StringBuilder();
        for (byte byt : data)
            results.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        return results.toString();
    }
}
