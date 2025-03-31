package cz.miou.h2.mariadb.misc;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.jdbc.JdbcConnection;

import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <a href="https://mariadb.com/kb/en/uuid_short/">UUID_SHORT</a>
 */
public class UuidShort implements FunctionDefinition {

    private static final AtomicLong counter = new AtomicLong();
    
    @Override
    public String getName() {
        return "UUID_SHORT";
    }

    @Override
    public String getMethodName() {
        return "uuidShort";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static long uuidShort(JdbcConnection connection) {
        var serverId = 1L;
        var serverStartupTime = ManagementFactory.getRuntimeMXBean().getStartTime();

        return  (serverId & 255) << 56
            + (serverStartupTime << 24)
            + counter.incrementAndGet();
    }
    
}