package cz.miou.h2.mariadb.hash;

import cz.miou.h2.api.FunctionDefinition;

import java.util.zip.CRC32C;

/**
 * <a href="https://mariadb.com/kb/en/crc32c/">CRC32C</a>
 */
public class Crc32c implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "CRC32C";
    }

    @Override
    public String getMethodName() {
        return "crc32c";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Long crc32c(String expr) {
        if (expr == null) {
            return null;
        }

        var hash = new CRC32C();
        hash.update(expr.getBytes());
        return hash.getValue();
    }

    @SuppressWarnings("unused")
    public static Long crc32c(Long par, String expr) {
        if (par == null || expr == null) {
            return null;
        }

        var hash = HashUtil.crcWithInitialValue(new CRC32C(), "crc", ~par.intValue());
        hash.update(expr.getBytes());
        return hash.getValue();
    }

}