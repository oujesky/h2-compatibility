package cz.miou.h2.mariadb.string;

import cz.miou.h2.api.FunctionDefinition;

import java.util.Base64;

/**
 * <a href="https://mariadb.com/kb/en/to_base64/">TO_BASE64</a>
 */
public class ToBase64 implements FunctionDefinition {

    @Override
    public String getName() {
        return "TO_BASE64";
    }

    @Override
    public String getMethodName() {
        return "toBase64";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static String toBase64(String text) {
        try {
            return Base64.getMimeEncoder().encodeToString(text.getBytes());
        } catch (Exception e) {
            return null;
        }
    }
}
