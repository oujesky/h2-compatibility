package cz.miou.h2.mariadb.string;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.util.StringUtils;
import org.h2.value.Value;
import org.h2.value.ValueVarchar;

/**
 * <a href="https://mariadb.com/kb/en/hex/">HEX</a>
 */
public class Hex implements FunctionDefinition {

    @Override
    public String getName() {
        return "HEX";
    }

    @Override
    public String getMethodName() {
        return "hex";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static String hex(Value input) {
        if (input instanceof ValueVarchar) {
            return StringUtils.convertBytesToHex(input.getBytes()).toUpperCase();
        }

        return ConversionUtil.convert(input, 10, 16);
    }
}
