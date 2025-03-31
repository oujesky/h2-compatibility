package cz.miou.h2.mariadb.hash;

import cz.miou.h2.api.FunctionDefinition;

import java.sql.Connection;

/**
 * <a href="https://mariadb.com/kb/en/md5/">MD5</a>
 */
public class Md5 implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "MD5";
    }

    @Override
    public String getMethodName() {
        return "md5";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static String md5(Connection connection, String str) {
        if (str == null) {
            return null;
        }

        return HashUtil.hash(str, "MD5");
    }
    
}