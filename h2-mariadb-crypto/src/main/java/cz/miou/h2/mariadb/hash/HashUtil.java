package cz.miou.h2.mariadb.hash;

import org.h2.util.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.Checksum;

class HashUtil {

    private HashUtil() {}

    static String hash(String text, String algorithm) {
        try {
            var digest = MessageDigest.getInstance(algorithm);
            digest.update(text.getBytes());
            return StringUtils.convertBytesToHex(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static <T extends Checksum> T crcWithInitialValue(T hash, String fieldName, int initialValue) {
        try {
            // hack: there is no easy way how to set the initial value for CRC32 computation
            var field = hash.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(hash, initialValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
        return hash;
    }
}
