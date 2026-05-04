package cz.miou.h2.api;

/**
 * Definition of an extension loaded as a database function
 * <p>
 * <a href="https://www.h2database.com/html/commands.html#create_alias">H2 CREATE ALIAS</a>
 */
public interface ExtensionLoader {

    /**
     * Name of the extension function
     */
    String getName();

    /**
     * Name of public class implementing the extension loader
     */
    String getClassName();

    /**
     * Name of a public static method declared on the extension class, that would be used for execution of the extension loader
     */
    String getMethodName();

}
