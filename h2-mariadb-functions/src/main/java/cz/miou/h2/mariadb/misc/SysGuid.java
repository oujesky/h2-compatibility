package cz.miou.h2.mariadb.misc;

import cz.miou.h2.api.FunctionDefinition;

import java.util.UUID;

/**
 * <a href="https://mariadb.com/kb/en/sys_guid/">SYS_GUID</a>
 */
public class SysGuid implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "SYS_GUID";
    }

    @Override
    public String getMethodName() {
        return "sysGuid";
    }

    @SuppressWarnings("unused")
    public static String sysGuid() {
        return UUID.randomUUID()
            .toString()
            .toUpperCase()
            .replace("-", "");
    }
    
}