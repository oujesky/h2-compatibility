package cz.miou.h2.mariadb.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

class AesUtil {

    public static final String DEFAULT_MODE = "aes-128-ecb";

    private AesUtil() {}

    public static byte[] encrypt(byte[] str, byte[] key, byte[] iv, String mode) {
        return run(Cipher.ENCRYPT_MODE, str, key, iv, mode);
    }

    public static byte[] decrypt(byte[] str, byte[] key, byte[] iv, String mode) {
        return run(Cipher.DECRYPT_MODE, str, key, iv, mode);
    }

    private static byte[] run(int opMode, byte[] str, byte[] key, byte[] iv, String mode) {
        if (str == null || key == null || mode == null) {
            return null;
        }

        var blockMode = AesMode.fromString(mode);
        if (blockMode == null) {
            return null;
        }

        switch (blockMode.getMode()) {
            case ECB:
                return run(opMode, str, key, blockMode.getLength(), new byte[16], "AES/CBC/PKCS5Padding");
            case CBC:
                return run(opMode, str, key, blockMode.getLength(), iv, "AES/CBC/PKCS5Padding");
            case CTR:
                return run(opMode, str, key, blockMode.getLength(), iv, "AES/CTR/PKCS5Padding");
            default:
                return null;
        }
    }

    public static byte[] run(int direction, byte[] str, byte[] key, int length, byte[] iv, String mode) {
        try {
            var keySpec = prepareKey(key, length);
            var ivSpec = new IvParameterSpec(iv);
            var cipher = Cipher.getInstance(mode);
            cipher.init(direction, keySpec, ivSpec);
            return cipher.doFinal(str);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException | InvalidAlgorithmParameterException e) {
            return null;
        }
    }

    private static SecretKeySpec prepareKey(byte[] key, int length) {
        var bytes = length / 8;
        var paddedKey = new byte[bytes];
        var i = 0;
        for (byte b : key) {
            paddedKey[i++ % bytes] ^= b;
        }
        return new SecretKeySpec(paddedKey, "AES");
    }

    private static class AesMode {

        private static final Pattern MODE_PATTERN = Pattern.compile("^aes-(?<length>128|192|256)-(?<mode>ecb|cbc|ctr)$");

        private final int length;
        private final Mode mode;

        public AesMode(int length, Mode mode) {
            this.length = length;
            this.mode = mode;
        }

        public static AesMode fromString(String input) {
            var matcher = MODE_PATTERN.matcher(input);
            if (!matcher.matches()) {
                return null;
            }

            var length = Integer.parseInt(matcher.group("length"));
            var mode = Mode.valueOf(matcher.group("mode").toUpperCase());

            return new AesMode(length, mode);
        }

        public int getLength() {
            return length;
        }

        public Mode getMode() {
            return mode;
        }
    }

    private enum Mode {
        ECB,
        CBC,
        CTR
    }
}
