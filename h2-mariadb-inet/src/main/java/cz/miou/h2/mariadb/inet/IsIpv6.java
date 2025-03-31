package cz.miou.h2.mariadb.inet;

import cz.miou.h2.api.FunctionDefinition;
import inet.ipaddr.IPAddressString;

/**
 * <a href="https://mariadb.com/kb/en/is_ipv6/">IS_IPV6</a>
 */
public class IsIpv6 implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "IS_IPV6";
    }

    @Override
    public String getMethodName() {
        return "isIpv6";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static Boolean isIpv6(String expr) {
        if (expr == null) {
            return null;
        }

        return new IPAddressString(expr).isIPv6();
    }
    
}