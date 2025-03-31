package cz.miou.h2.mariadb.inet;

import cz.miou.h2.api.FunctionDefinition;
import inet.ipaddr.IPAddressString;

/**
 * <a href="https://mariadb.com/kb/en/is_ipv4/">IS_IPV4</a>
 */
public class IsIpv4 implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "IS_IPV4";
    }

    @Override
    public String getMethodName() {
        return "isIpv4";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static Boolean isIpv4(String expr) {
        if (expr == null) {
            return null;
        }

        return new IPAddressString(expr).isIPv4();
    }
    
}