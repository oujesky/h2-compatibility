package cz.miou.h2.mariadb.crypto;

import cz.miou.h2.api.FunctionDefinition;

/**
 * <a href="https://mariadb.com/kb/en/aes_encrypt/">AES_ENCRYPT</a>
 */
public class AesEncrypt implements FunctionDefinition {

    @Override
    public String getName() {
        return "AES_ENCRYPT";
    }

    @Override
    public String getMethodName() {
        return "aesEncrypt";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static byte[] aesEncrypt(byte[] str, byte[] key, byte[] iv, String mode) {
        return AesUtil.encrypt(str, key, iv, mode);
    }

    @SuppressWarnings("unused")
    public static byte[] aesEncrypt(byte[] str, byte[] key, byte[] iv) {
        return aesEncrypt(str, key, iv, AesUtil.DEFAULT_MODE);
    }

    @SuppressWarnings("unused")
    public static byte[] aesEncrypt(byte[] str, byte[] key) {
        return aesEncrypt(str, key, new byte[16]);
    }

}