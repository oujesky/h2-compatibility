package cz.miou.h2.mariadb.string;

import cz.miou.h2.api.FunctionDefinition;

import java.util.regex.Pattern;

/**
 * <a href="https://mariadb.com/kb/en/regexp_instr/">REGEXP_INSTR</a>
 */
public class RegexpInstr implements FunctionDefinition {

    @Override
    public String getName() {
        return "REGEXP_INSTR";
    }

    @Override
    public String getMethodName() {
        return "regexpInstr";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Integer regexpInstr(String subject, String pattern) {
        if (subject == null || pattern == null) {
            return null;
        }

        var matcher = Pattern.compile(pattern).matcher(subject);
        if (matcher.find()) {
            return matcher.start() + 1;
        } else {
            return 0;
        }
    }
}
