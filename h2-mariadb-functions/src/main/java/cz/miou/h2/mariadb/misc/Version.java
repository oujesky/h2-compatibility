package cz.miou.h2.mariadb.misc;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.engine.Constants;

/**
 * <a href="https://mariadb.com/kb/en/version/">VERSION</a>
 */
public class Version implements FunctionDefinition {

    @Override
    public String getName() {
        return "VERSION";
    }

    @Override
    public String getMethodName() {
        return "version";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static String version() {
        return Constants.VERSION + "-H2";
    }
}
