package cz.miou.h2.mariadb.misc;

import cz.miou.h2.api.FunctionDefinition;

/**
 * <a href="https://mariadb.com/kb/en/sleep/">SLEEP</a>
 */
public class Sleep implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "SLEEP";
    }

    @Override
    public String getMethodName() {
        return "sleep";
    }

    @SuppressWarnings("unused")
    public static int sleep(Integer duration) {
        if (duration == null || duration <= 0) {
            return 0;
        }

        try {
            Thread.sleep(duration * 1000L);
            return 0;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return 1;
        }
    }
    
}