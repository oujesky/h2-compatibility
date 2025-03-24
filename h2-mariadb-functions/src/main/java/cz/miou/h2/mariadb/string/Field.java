package cz.miou.h2.mariadb.string;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <a href="https://mariadb.com/kb/en/field/">FIELD</a>
 */
public class Field implements FunctionDefinition {

    @Override
    public String getName() {
        return "FIELD";
    }

    @Override
    public String getMethodName() {
        return "field";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static int field(Value pattern, Value...values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Incorrect parameter count");
        }

        if (pattern == null) {
            return 0;
        }

        var lowerCasePattern = pattern.getString().toLowerCase();
        var lowerCaseValues = Stream.of(values)
            .map(Value::getString)
            .map(String::toLowerCase)
            .collect(Collectors.toUnmodifiableList());

        return 1 + lowerCaseValues.indexOf(lowerCasePattern);
    }
}
