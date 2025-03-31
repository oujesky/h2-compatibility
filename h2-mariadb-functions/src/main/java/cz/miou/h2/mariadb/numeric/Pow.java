package cz.miou.h2.mariadb.numeric;

import cz.miou.h2.api.FunctionDefinition;

/**
 * <a href="https://mariadb.com/kb/en/pow/">POW</a>
 */
public class Pow implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "POW";
    }

    @Override
    public String getMethodName() {
        return "pow";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static Double pow(Double x, Double y) {
        if (x == null || y == null) {
            return null;
        }

        return Math.pow(x, y);
    }
    
}