package cz.miou.h2.mariadb.misc;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.tools.CompressTool;

/**
 * <a href="https://mariadb.com/kb/en/uncompressed_length/">UNCOMPRESSED_LENGTH</a>
 */
public class UncompressedLength implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "UNCOMPRESSED_LENGTH";
    }

    @Override
    public String getMethodName() {
        return "uncompressedLength";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static Integer uncompressedLength(byte[] compressed) {
        if (compressed == null) {
            return null;
        }

        return CompressTool.getInstance().expand(compressed).length;
    }
    
}