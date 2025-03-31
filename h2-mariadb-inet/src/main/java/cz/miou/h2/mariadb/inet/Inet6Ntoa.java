package cz.miou.h2.mariadb.inet;

import cz.miou.h2.api.FunctionDefinition;
import inet.ipaddr.AddressValueException;
import inet.ipaddr.IPAddress;
import inet.ipaddr.ipv4.IPv4Address;
import inet.ipaddr.ipv6.IPv6Address;

/**
 * <a href="https://mariadb.com/kb/en/inet6_ntoa/">INET6_NTOA</a>
 */
public class Inet6Ntoa implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "INET6_NTOA";
    }

    @Override
    public String getMethodName() {
        return "inet6Ntoa";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static String inet6Ntoa(byte[] expr) {
        if (expr == null) {
            return null;
        }

        var address= getIpAddress(expr);

        if (address == null) {
            return null;
        }

        return address.toString();
    }

    private static IPAddress getIpAddress(byte[] expr) {
        try {
            return new IPv4Address(expr);
        } catch (AddressValueException e) {
            try {
                return new IPv6Address(expr);
            } catch (AddressValueException ex) {
                return null;
            }
        }
    }

}