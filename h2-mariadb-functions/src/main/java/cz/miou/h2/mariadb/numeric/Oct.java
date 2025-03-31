package cz.miou.h2.mariadb.numeric;

import cz.miou.h2.api.FunctionDefinition;
import cz.miou.h2.mariadb.util.ConversionUtil;
import org.h2.value.Value;

/**
 * <a href="https://mariadb.com/kb/en/oct/">OCT</a>
 */
public class Oct implements FunctionDefinition {

    @Override
    public String getName() {
        return "OCT";
    }

    @Override
    public String getMethodName() {
        return "oct";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static String oct(Value value) {
        if (value == null) {
            return null;
        }

        return ConversionUtil.convert(value, 10, 8);
    }
}
