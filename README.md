# h2-compatibility

Library of functions to extend [H2](https://www.h2database.com/) database with functions missing in compatibility modes.

Mainly for simplified integration testing in cases where running a real target database in container or standalone could be considered an overkill.

Aim is to provide missing functions, aggregations and types as compatible as possible, with minimal additional dependencies and targeting same minimal JDK version as H2 project, which is currently Java 11.

Functions are split into thematic "packs" and are dynamically registered using the Java `ServiceLoader` mechanism.

## Usage

### 1. Include required function "packs"

#### Gradle
```kotlin
testImplementation("cz.miou.h2:h2-[DB]-[FUNCTIONS]:1.0.0")
```

#### Maven

```xml
<dependency>
    <groupId>cz.miou.h2</groupId>
    <artifactId>h2-[DB]-[FUNCTIONS]</artifactId>
    <version>1.0.0</version>
    <scope>test</scope>
</dependency>
```

### 2. Load functions to database

#### From SQL code

```sql
CREATE ALIAS IF NOT EXISTS H2_COMPATIBILITY FOR "cz.miou.h2.loader.Loader.load";
CALL H2_COMPATIBILITY();
```

#### From Java code

```java
import java.sql.Connection;
import cz.miou.h2.Loader;

...

Connection connection = ... // JDBC connection for H2 database
Loader.load(connection);

```

#### Troubleshooting

If for some reason some of the functions aren't compatible with H2 anymore, it's possible to exclude them by specifying `cz.miou.h2.loader.exclude` system property with a coma separated list of function names.

## Supported Functions

TODO
## License

[MIT](https://choosealicense.com/licenses/mit/)

