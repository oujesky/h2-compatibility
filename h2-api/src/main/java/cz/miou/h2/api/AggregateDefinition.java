package cz.miou.h2.api;

/**
 * Definition of an aggregate function
 * <p>
 * <a href="https://www.h2database.com/html/commands.html#create_aggregate">H2 CREATE AGGREGATE</a>
 *
 * @implSpec The implementation must implement either {@link org.h2.api.Aggregate} or {@link org.h2.api.AggregateFunction}
 */
public interface AggregateDefinition {

    /**
     * Name of the registered aggregate function
     */
    String getName();
}
