# h2-compatibility

Library of functions to extend [H2](https://www.h2database.com/) database with functions missing in compatibility modes.

Mainly for simplified integration testing in cases where running a real target database in container or standalone could be considered an overkill.

Aim is to provide missing functions, aggregations and types as compatible as possible, with minimal additional dependencies and targeting same minimal JDK version as H2 project, which is currently Java 11.

Functions are split into modules and are dynamically registered using the Java `ServiceLoader` mechanism.

## Usage

### 1. Include required modules

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

### MariaDB / MySQL

#### Date / Time functions

Module `h2-mariadb-functions`

<details>

<summary>List of supported functions</summary>

* [`ADDDATE`](https://mariadb.com/kb/en/adddate/)
* [`ADD_MONTHS`](https://mariadb.com/kb/en/add_months/)
* [`ADDTIME`](https://mariadb.com/kb/en/addtime/)
* [`CONVERT_TZ`](https://mariadb.com/kb/en/convert_tz/)
* [`DATE_ADD`](https://mariadb.com/kb/en/date_add/)
* [`DATE_FORMAT`](https://mariadb.com/kb/en/date_format/)
* [`DATE_SUB`](https://mariadb.com/kb/en/date_sub/)
* [`FORMAT_PICO_TIME`](https://mariadb.com/kb/en/format_pico_time/)
* [`FROM_DAYS`](https://mariadb.com/kb/en/from_days/)
* [`MAKEDATE`](https://mariadb.com/kb/en/makedate/)
* [`MAKETIME`](https://mariadb.com/kb/en/maketime/)
* [`MICROSECOND`](https://mariadb.com/kb/en/microsecond/)
* [`PERIOD_ADD`](https://mariadb.com/kb/en/period_add/)
* [`PERIOD_DIFF`](https://mariadb.com/kb/en/period_diff/)
* [`SEC_TO_TIME`](https://mariadb.com/kb/en/sec_to_time/)
* [`STR_TO_DATE`](https://mariadb.com/kb/en/str_to_date/)
* [`SUBDATE`](https://mariadb.com/kb/en/subdate/)
* [`SUBTIME`](https://mariadb.com/kb/en/subtime/)
* [`TIME`](https://mariadb.com/kb/en/time/)
* [`TIMEDIFF`](https://mariadb.com/kb/en/timediff/)
* [`TIME_FORMAT`](https://mariadb.com/kb/en/time_format/)
* [`TIMESTAMP`](https://mariadb.com/kb/en/timestamp/)
* [`TIME_TO_SEC`](https://mariadb.com/kb/en/time_to_sec/)
* [`TO_DAYS`](https://mariadb.com/kb/en/to_days/)
* [`TO_SECONDS`](https://mariadb.com/kb/en/to_seconds/)
* [`UTC_DATE`](https://mariadb.com/kb/en/utc_date/)
* [`UTC_TIME`](https://mariadb.com/kb/en/utc_time/)
* [`UTC_TIMESTAMP`](https://mariadb.com/kb/en/utc_timestamp/)
* [`WEEKDAY`](https://mariadb.com/kb/en/weekday/)
* [`WEEKOFYEAR`](https://mariadb.com/kb/en/weekofyear/)
* [`YEARWEEK`](https://mariadb.com/kb/en/yearweek/)

</details>

##### Notes

Due to how `DATE`, `TIME` and `TIMESTAMP` types in H2 works, the functions behave as if MariaDB had
[`ALLOW_INVALID_DATES`](https://mariadb.com/kb/en/sql-mode/#allow_invalid_dates), [`NO_ZERO_DATE`](https://mariadb.com/kb/en/sql-mode/#no_zero_date) and [`NO_ZERO_IN_DATE`](https://mariadb.com/kb/en/sql-mode/#no_zero_in_date) SQL modes disabled.

## License

[MIT](https://choosealicense.com/licenses/mit/)

