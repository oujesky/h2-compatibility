package cz.miou.h2.mariadb.hash;

import cz.miou.h2.api.FunctionDefinition;

import java.util.zip.CRC32;

/**
 * <a href="https://mariadb.com/kb/en/crc32/">CRC32</a>
 */
public class Crc32 implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "CRC32";
    }

    @Override
    public String getMethodName() {
        return "crc32";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Long crc32(String expr) {
        if (expr == null) {
            return null;
        }

        var hash = new CRC32();
        hash.update(expr.getBytes());
        return hash.getValue();
    }

    @SuppressWarnings("unused")
    public static Long crc32(Long par, String expr) {
        if (par == null || expr == null) {
            return null;
        }

        var hash = HashUtil.crcWithInitialValue(new CRC32(), "crc", par.intValue());
        hash.update(expr.getBytes());
        return hash.getValue();
    }

}