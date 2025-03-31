package cz.miou.h2.mariadb.string;

import cz.miou.h2.api.FunctionDefinition;
import cz.miou.h2.mariadb.util.ConversionUtil;
import org.h2.util.StringUtils;
import org.h2.value.Value;
import org.h2.value.ValueBinary;
import org.h2.value.ValueChar;
import org.h2.value.ValueVarbinary;
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
        if (input instanceof ValueChar || input instanceof ValueVarchar || input instanceof ValueBinary || input instanceof ValueVarbinary) {
            return StringUtils.convertBytesToHex(input.getBytes()).toUpperCase();
        }

        return ConversionUtil.convert(input, 10, 16);
    }
}
