package cz.miou.h2.mariadb.string;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.util.StringUtils;
import org.h2.value.Value;

/**
 * <a href="https://mariadb.com/kb/en/unhex/">UNHEX</a>
 */
public class UnHex implements FunctionDefinition {

    @Override
    public String getName() {
        return "UNHEX";
    }

    @Override
    public String getMethodName() {
        return "unHex";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static byte[] unHex(Value input) {
        return StringUtils.convertHexToBytes(input.getString());
    }
}
