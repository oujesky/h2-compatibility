package cz.miou.h2.mariadb.numeric;

import cz.miou.h2.api.FunctionDefinition;

/**
 * <a href="https://mariadb.com/kb/en/log2/">LOG2</a>
 */
public class Log2 implements FunctionDefinition {

    @Override
    public String getName() {
        return "LOG2";
    }

    @Override
    public String getMethodName() {
        return "log2";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Double log2(Long number) {
        if (number == null || number <= 0) {
            return null;
        }

        return Math.log(number) / Math.log(2);
    }
}
