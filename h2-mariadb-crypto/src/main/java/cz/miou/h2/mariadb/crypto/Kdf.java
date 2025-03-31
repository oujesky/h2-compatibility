package cz.miou.h2.mariadb.crypto;

import at.favre.lib.hkdf.HKDF;
import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.h2.value.ValueInteger;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * <a href="https://mariadb.com/kb/en/kdf/">KDF</a>
 */
public class Kdf implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "KDF";
    }

    @Override
    public String getMethodName() {
        return "kdf";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static byte[] kdf(String key, byte[] salt, Value info, String name, Integer width) {
        if (key == null || salt == null || info == null || name == null || width == null) {
            return null;
        }

        switch (name) {
            case "pbkdf2_hmac":
                return pbkdf2Hmac(key, salt, info.getInt(), width);
            case "hkdf":
                return hkdf(key.getBytes(), salt, info.getBytes(), width);
            default:
                return null;
        }
    }

    @SuppressWarnings("unused")
    public static byte[] kdf(String key, byte[] salt, Value info, String name) {
        return kdf(key, salt, info, name, 128);
    }

    @SuppressWarnings("unused")
    public static byte[] kdf(String key, byte[] salt, Value info) {
        return kdf(key, salt, info, "pbkdf2_hmac");
    }

    @SuppressWarnings("unused")
    public static byte[] kdf(String key, byte[] salt) {
        return kdf(key, salt, ValueInteger.get(1000));
    }

    private static byte[] pbkdf2Hmac(String key, byte[] salt, int iterations, int width) {
        try {
            var keySpec = new PBEKeySpec(
                key.toCharArray(),
                salt,
                iterations,
                width
            );

            var keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");

            return keyFactory.generateSecret(keySpec).getEncoded();
        } catch (RuntimeException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            return null;
        }
    }

    private static byte[] hkdf(byte[] key, byte[] salt, byte[] info, int width) {
        var hkdf = HKDF.fromHmacSha512();
        var pseudoRandomKey = hkdf.extract(salt, key);
        return hkdf.expand(pseudoRandomKey, info, width / 8);
    }

}