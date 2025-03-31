package cz.miou.h2.mariadb.hash;

import cz.miou.h2.api.FunctionDefinition;

/**
 * <a href="https://mariadb.com/kb/en/sha2/">SHA2</a>
 */
public class Sha2 implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "SHA2";
    }

    @Override
    public String getMethodName() {
        return "sha2";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static String sha2(String str, Integer length) {
        if (str == null) {
            return null;
        }

        if (length == 0) {
            length = 256;
        }

        switch (length) {
            case 224:
            case 256:
            case 384:
            case 512:
                return HashUtil.hash(str, String.format("SHA-%d", length));

            default:
                return null;
        }
    }

}