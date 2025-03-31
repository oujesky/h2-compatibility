package cz.miou.h2.mariadb.inet;

import cz.miou.h2.api.FunctionDefinition;
import inet.ipaddr.AddressStringException;
import inet.ipaddr.IPAddressString;

/**
 * <a href="https://mariadb.com/kb/en/inet6_aton/">INET6_ATON</a>
 */
public class Inet6Aton implements FunctionDefinition {

    @Override
    public String getName() {
        return "INET6_ATON";
    }

    @Override
    public String getMethodName() {
        return "inet6Aton";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static byte[] inet6Aton(String expr) {
        if (expr == null || expr.isEmpty()) {
            return null;
        }

        var address = new IPAddressString(expr);
        if (!address.isIPv4() && !address.isIPv6()) {
            return null;
        }

        try {
            return address.toAddress().getBytes();
        } catch (AddressStringException e) {
            return null;
        }
    }

}