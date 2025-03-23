package cz.miou.h2.api;

/**
 * Type alias definition
 * <p>
 * <a href="https://www.h2database.com/html/commands.html#create_domain">H2 CREATE DOMAIN</a>
 */
public interface TypeDefinition {

    /**
     * Name of the type alias
     */
    String getName();

    /**
     * Name of the type aliased
     */
    String getType();
}
