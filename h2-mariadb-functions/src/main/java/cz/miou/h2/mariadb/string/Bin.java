package cz.miou.h2.mariadb.string;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;

/**
 * <a href="https://mariadb.com/kb/en/bin/">BIN</a>
 */
public class Bin implements FunctionDefinition {

    @Override
    public String getName() {
        return "BIN";
    }

    @Override
    public String getMethodName() {
        return "bin";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static String bin(Value input) {
        return ConversionUtil.convert(input, 10, 2);
    }
}
