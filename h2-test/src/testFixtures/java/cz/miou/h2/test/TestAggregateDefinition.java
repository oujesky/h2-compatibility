package cz.miou.h2.test;

import cz.miou.h2.api.AggregateDefinition;
import org.h2.api.AggregateFunction;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class TestAggregateDefinition implements AggregateDefinition, AggregateFunction {

    private final List<String> values = new ArrayList<>();

    @Override
    public String getName() {
        return "TEST_AGGREGATE";
    }

    @Override
    public int getType(int[] inputTypes) {
        return Types.VARCHAR;
    }

    @Override
    public void add(Object value) {
        values.add((String) value);
    }

    @Override
    public Object getResult() {
        return String.join(",", values);
    }
}
