package cz.miou.h2.mariadb.inet;

import cz.miou.h2.api.FunctionDefinition;
import inet.ipaddr.ipv6.IPv6Address;


/**
 * <a href="https://mariadb.com/kb/en/is_ipv4_compat/">IS_IPV4_COMPAT</a>
 */
public class IsIpv4Compat implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "IS_IPV4_COMPAT";
    }

    @Override
    public String getMethodName() {
        return "isIpv4Compat";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static Boolean isIpv4Compat(byte[] expr) {
        if (expr == null) {
            return null;
        }

        var address = new IPv6Address(expr);
        return address.isIPv4Compatible();
    }
    
}