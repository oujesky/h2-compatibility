package cz.miou.h2.mariadb.inet;

import cz.miou.h2.api.FunctionDefinition;
import inet.ipaddr.IPAddressString;
import inet.ipaddr.ipv4.IPv4Address;

/**
 * <a href="https://mariadb.com/kb/en/inet_aton/">INET_ATON</a>
 */
public class InetAton implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "INET_ATON";
    }

    @Override
    public String getMethodName() {
        return "inetAton";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static Long inetAton(String ip) {
        if (ip == null || ip.isEmpty()) {
            return null;
        }

        var address = new IPAddressString(ip).getAddress();
        return address instanceof IPv4Address
            ? ((IPv4Address) address).longValue()
            : null;
    }

}