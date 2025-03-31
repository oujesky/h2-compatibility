package cz.miou.h2.mariadb.hash;

import cz.miou.h2.api.FunctionDefinition;

/**
 * <a href="https://mariadb.com/kb/en/sha1/">SHA1</a>
 */
public class Sha1 implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "SHA1";
    }

    @Override
    public String getMethodName() {
        return "sha1";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static String sha1(String str) {
        if (str == null) {
            return null;
        }

        return HashUtil.hash(str, "SHA1");
    }
    
}