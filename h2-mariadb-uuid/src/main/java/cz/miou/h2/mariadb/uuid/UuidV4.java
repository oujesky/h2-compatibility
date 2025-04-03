package cz.miou.h2.mariadb.uuid;

import com.fasterxml.uuid.Generators;
import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.ValueUuid;

/**
 * <a href="https://mariadb.com/kb/en/uuidv4/">UUIDV4</a>
 */
public class UuidV4 implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "UUIDV4";
    }

    @Override
    public String getMethodName() {
        return "uuidV4";
    }

    @SuppressWarnings("unused")
    public static ValueUuid uuidV4() {
        return ValueUuid.get(Generators.randomBasedGenerator().generate());
    }
    
}