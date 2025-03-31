package cz.miou.h2.mariadb.misc;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.ValueUuid;

/**
 * <a href="https://mariadb.com/kb/en/uuidv7/">UUIDV7</a>
 */
public class UuidV7 implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "UUIDV7";
    }

    @Override
    public String getMethodName() {
        return "uuidV7";
    }

    @SuppressWarnings("unused")
    public static ValueUuid uuidV7() {
        return ValueUuid.getNewRandom(7);
    }
    
}