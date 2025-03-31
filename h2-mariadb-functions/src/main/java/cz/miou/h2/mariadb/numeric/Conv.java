package cz.miou.h2.mariadb.numeric;

import cz.miou.h2.api.FunctionDefinition;
import cz.miou.h2.mariadb.util.ConversionUtil;
import org.h2.value.Value;

/**
 * <a href="https://mariadb.com/kb/en/conv/">CONV</a>
 */
public class Conv implements FunctionDefinition {

    private static final int BASE_MIN = 2;
    private static final int BASE_MAX = 62;

    @Override
    public String getName() {
        return "CONV";
    }

    @Override
    public String getMethodName() {
        return "conv";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static String conv(Value value, Integer fromBase, Integer toBase) {
        if (fromBase == null || toBase == null) {
            return null;
        }

        var signed = false;
        if (toBase < 0) {
            toBase = Math.abs(toBase);
            signed = true;
        }

        if (fromBase < BASE_MIN || toBase < BASE_MIN || fromBase > BASE_MAX || toBase > BASE_MAX) {
            return null;
        }

        return ConversionUtil.convert(value, fromBase, toBase, signed);
    }
}
