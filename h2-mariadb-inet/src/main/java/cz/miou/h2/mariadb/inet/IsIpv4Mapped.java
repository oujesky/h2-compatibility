package cz.miou.h2.mariadb.inet;

import cz.miou.h2.api.FunctionDefinition;
import inet.ipaddr.ipv6.IPv6Address;

/**
 * <a href="https://mariadb.com/kb/en/is_ipv4_mapped/">IS_IPV4_MAPPED</a>
 */
public class IsIpv4Mapped implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "IS_IPV4_MAPPED";
    }

    @Override
    public String getMethodName() {
        return "isIpv4Mapped";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static Boolean isIpv4Mapped(byte[] expr) {
        if (expr == null) {
            return null;
        }

        var address = new IPv6Address(expr);
        return address.isIPv4Mapped();
    }
    
}