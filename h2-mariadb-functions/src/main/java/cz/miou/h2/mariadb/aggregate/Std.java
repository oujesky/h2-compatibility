package cz.miou.h2.mariadb.aggregate;

import cz.miou.h2.api.AggregateDefinition;
import org.h2.api.AggregateFunction;

import java.sql.Types;

/**
 * <a href="https://mariadb.com/kb/en/std/">STD</a>
 */
public class Std implements AggregateDefinition, AggregateFunction {

    private long count;
    private double mean;
    private double sumSquaredDistance;

    @Override
    public String getName() {
        return "STD";
    }

    @Override
    public int getType(int[] inputTypes) {
        return Types.DOUBLE;
    }

    @Override
    public void add(Object value) {
        if (!(value instanceof Number)) {
            return;
        }

        var number = ((Number) value).doubleValue();
        count++;

        var delta = number - mean;
        mean += delta / count;
        sumSquaredDistance += delta * (number - mean);
    }

    @Override
    public Object getResult() {
        if (count == 0) {
            return null;
        }

        return Math.sqrt(sumSquaredDistance / count);
    }
}
