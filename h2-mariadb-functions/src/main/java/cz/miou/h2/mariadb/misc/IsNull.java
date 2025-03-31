package cz.miou.h2.mariadb.misc;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;

/**
 * <a href="https://mariadb.com/kb/en/isnull/">ISNULL</a>
 */
public class IsNull implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "ISNULL";
    }

    @Override
    public String getMethodName() {
        return "isNull";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static boolean isNull(Value expr) {
        return expr.containsNull();
    }
    
}