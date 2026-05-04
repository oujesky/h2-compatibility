package cz.miou.h2.mariadb.spatial.test;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.platform.commons.util.Preconditions;

import java.util.stream.Collectors;

public class WktAggregator implements ArgumentsAggregator {

    @Override
    public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {
        var type = context.getParameter().getType();
        Preconditions.condition(
            type.isAssignableFrom(WktGeometries.class),
            () -> String.format("Expected type %s but got %s", WktGeometries.class.getName(), type.getName())
        );

        var geometries = accessor.toList().stream()
            .skip(context.getIndex())
            .map(String::valueOf)
            .collect(Collectors.toList());

        return new WktGeometries(geometries);
    }
}
