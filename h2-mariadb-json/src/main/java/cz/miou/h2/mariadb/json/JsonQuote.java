package cz.miou.h2.mariadb.json;

import com.fasterxml.jackson.core.io.JsonStringEncoder;
import cz.miou.h2.api.FunctionDefinition;

/**
 * <a href="https://mariadb.com/kb/en/json_quote/">JSON_QUOTE</a>
 */
public class JsonQuote implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "JSON_QUOTE";
    }

    @Override
    public String getMethodName() {
        return "jsonQuote";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static String jsonQuote(String json) {
        if (json == null) {
            return null;
        }

        var encoder = JsonStringEncoder.getInstance();

        var sb = new StringBuilder();
        sb.append('"');
        encoder.quoteAsString(json, sb);
        sb.append('"');
        return sb.toString();
    }
    
}