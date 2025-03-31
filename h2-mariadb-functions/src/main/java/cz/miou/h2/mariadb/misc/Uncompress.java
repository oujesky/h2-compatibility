package cz.miou.h2.mariadb.misc;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.message.DbException;
import org.h2.tools.CompressTool;

import static org.h2.api.ErrorCode.UNSUPPORTED_COMPRESSION_ALGORITHM_1;

/**
 * <a href="https://mariadb.com/kb/en/uncompress/">UNCOMPRESS</a>
 */
public class Uncompress implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "UNCOMPRESS";
    }

    @Override
    public String getMethodName() {
        return "uncompress";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static byte[] uncompress(byte[] compressed) {
        if (compressed == null) {
            return null;
        }

        try {
            return CompressTool.getInstance().expand(compressed);
        } catch (DbException e) {
            if (e.getErrorCode() == UNSUPPORTED_COMPRESSION_ALGORITHM_1) {
                return null;
            }
            throw e;
        }
    }
    
}