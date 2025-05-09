package cz.miou.h2.api;

/**
 * Definition of a database function
 * <p>
 * <a href="https://www.h2database.com/html/commands.html#create_alias">H2 CREATE ALIAS</a>
 */
public interface FunctionDefinition {

    /**
     * Name of the registered function
     */
    String getName();

    /**
     * Name of a public static method declared on the implementing class, that would be used for execution of registered function
     */
    String getMethodName();

    /**
     * When true, registered function will be declared as DETERMINISTIC which tells the H2 engine that the call results might
     * be cached, since the function will return the same value for the same parameters.
     */
    default boolean isDeterministic() {
        return false;
    }
}
