package cz.miou.h2.mariadb.string;

import cz.miou.h2.api.FunctionDefinition;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * <a href="https://mariadb.com/kb/en/make_set/">MAKE_SET</a>
 */
public class MakeSet implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "MAKE_SET";
    }

    @Override
    public String getMethodName() {
        return "makeSet";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static String makeSet(Long bits, String... values) {
        if (bits == null || values == null || values.length == 0) {
            return null;
        }

        return IntStream.range(0, values.length)
            .filter(index -> ((bits >> index) & 1) > 0)
            .mapToObj(index -> values[index])
            .filter(Objects::nonNull)
            .collect(Collectors.joining(","));
    }
    
}