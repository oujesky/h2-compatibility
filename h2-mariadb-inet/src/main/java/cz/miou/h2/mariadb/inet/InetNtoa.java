package cz.miou.h2.mariadb.inet;

import cz.miou.h2.api.FunctionDefinition;
import inet.ipaddr.ipv4.IPv4Address;

/**
 * <a href="https://mariadb.com/kb/en/inet_ntoa/">INET_NTOA</a>
 */
public class InetNtoa implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "INET_NTOA";
    }

    @Override
    public String getMethodName() {
        return "inetNtoa";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static String inetNtoa(Long expr) {
        if (expr == null) {
            return null;
        }

        var addresss = new IPv4Address(expr.intValue());
        return addresss.toString();
    }

}