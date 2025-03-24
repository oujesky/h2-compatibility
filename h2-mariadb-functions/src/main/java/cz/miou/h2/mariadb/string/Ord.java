package cz.miou.h2.mariadb.string;

import cz.miou.h2.api.FunctionDefinition;

import java.nio.charset.StandardCharsets;

/**
 * <a href="https://mariadb.com/kb/en/ord/">ORD</a>
 */
public class Ord implements FunctionDefinition {

    @Override
    public String getName() {
        return "ORD";
    }

    @Override
    public String getMethodName() {
        return "ord";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Integer ord(String input) {
        if (input == null) {
            return null;
        }

        if (input.isEmpty()) {
            return 0;
        }

        var bytes = input.substring(0, 1).getBytes(StandardCharsets.UTF_8);

        var result = 0;
        for (byte b : bytes) {
            result = (result << 8) + (b & 0xFF);
        }

        return result;
    }
}
