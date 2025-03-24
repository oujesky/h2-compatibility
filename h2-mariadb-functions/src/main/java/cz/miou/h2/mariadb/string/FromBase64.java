package cz.miou.h2.mariadb.string;

import cz.miou.h2.api.FunctionDefinition;

import java.util.Base64;

/**
 * <a href="https://mariadb.com/kb/en/from_base64/">FROM_BASE64</a>
 */
public class FromBase64 implements FunctionDefinition {

    @Override
    public String getName() {
        return "FROM_BASE64";
    }

    @Override
    public String getMethodName() {
        return "fromBase64";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static String fromBase64(String base64) {
        try {
            return new String(Base64.getMimeDecoder().decode(base64));
        } catch (Exception e) {
            return null;
        }
    }
}
