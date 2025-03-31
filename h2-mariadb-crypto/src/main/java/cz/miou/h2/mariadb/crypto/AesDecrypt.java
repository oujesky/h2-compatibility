package cz.miou.h2.mariadb.crypto;

import cz.miou.h2.api.FunctionDefinition;

/**
 * <a href="https://mariadb.com/kb/en/aes_decrypt/">AES_DECRYPT</a>
 */
public class AesDecrypt implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "AES_DECRYPT";
    }

    @Override
    public String getMethodName() {
        return "aesDecrypt";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static byte[] aesDecrypt(byte[] cryptStr, byte[] key, byte[] iv, String mode) {
        return AesUtil.decrypt(cryptStr, key, iv, mode);
    }

    public static byte[] aesDecrypt(byte[] cryptStr, byte[] key, byte[] iv) {
        return aesDecrypt(cryptStr, key, iv, AesUtil.DEFAULT_MODE);
    }

    public static byte[] aesDecrypt(byte[] cryptStr, byte[] key) {
        return aesDecrypt(cryptStr, key, new byte[16]);
    }
}