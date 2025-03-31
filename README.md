# h2-compatibility

Library of functions to extend [H2](https://www.h2database.com/) database with functions missing in compatibility modes.

Mainly for simplified integration testing in cases where running a real target database in container or standalone could be considered an overkill.

Aim is to provide missing functions, aggregations and types as compatible as possible, with minimal additional dependencies and targeting same minimal JDK version as H2 project, which is currently Java 11. Minimum H2 version that is compatible is 2.2.x.

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

#### String functions

Module `h2-mariadb-functions`

<details>

<summary>List of supported functions</summary>

* [`BIN`](https://mariadb.com/kb/en/bin/)
* [`ELT`](https://mariadb.com/kb/en/elt/)
* [`EXPORT_SET`](https://mariadb.com/kb/en/export_set/)
* [`FIELD`](https://mariadb.com/kb/en/field/)
* [`FIND_IN_SET`](https://mariadb.com/kb/en/find_in_set/)
* [`FORMAT`](https://mariadb.com/kb/en/format/)
* [`FROM_BASE64`](https://mariadb.com/kb/en/from_base64/)
* [`HEX`](https://mariadb.com/kb/en/hex/)
* [`LOAD_FILE`](https://mariadb.com/kb/en/load_file/)
* [`MAKE_SET`](https://mariadb.com/kb/en/make_set/)
* [`MID`](https://mariadb.com/kb/en/mid/)
* [`NATURAL_SORT_KEY`](https://mariadb.com/kb/en/natural_sort_key/)
* [`ORD`](https://mariadb.com/kb/en/ord/)
* [`QUOTE`](https://mariadb.com/kb/en/quote/)
* [`REGEXP_INSTR`](https://mariadb.com/kb/en/regexp_instr/)
* [`REVERSE`](https://mariadb.com/kb/en/reverse/)
* [`STRCMP`](https://mariadb.com/kb/en/strcmp/)
* [`SUBSTRING_INDEX`](https://mariadb.com/kb/en/substring_index/)
* [`TO_BASE64`](https://mariadb.com/kb/en/to_base64/)
* [`UNHEX`](https://mariadb.com/kb/en/unhex/)

</details>

#### Numeric, binary and misc. functions

Module `h2-mariadb-functions`

<details>

<summary>List of supported functions</summary>

* [`BIT_COUNT`](https://mariadb.com/kb/en/bit_count/)
* [`CONV`](https://mariadb.com/kb/en/conv/)
* [`FORMAT_BYTES`](https://mariadb.com/kb/en/miscellaneous-functions-format_bytes/)
* [`ISNULL`](https://mariadb.com/kb/en/isnull/)
* [`LOG2`](https://mariadb.com/kb/en/log2/)
* [`OCT`](https://mariadb.com/kb/en/oct/)
* [`POW`](https://mariadb.com/kb/en/pow/)
* [`SLEEP`](https://mariadb.com/kb/en/sleep/)
* [`SYS_GUID`](https://mariadb.com/kb/en/sys_guid/)
* [`UNCOMPRESSED_LENGTH`](https://mariadb.com/kb/en/uncompressed_length/)
* [`UNCOMPRESS`](https://mariadb.com/kb/en/uncompress/)
* [`UUIDV4`](https://mariadb.com/kb/en/uuidv4/)
* [`UUIDV7`](https://mariadb.com/kb/en/uuidv7/)
* [`UUID_SHORT`](https://mariadb.com/kb/en/uuid_short/)

</details>

#### Date / Time functions

Module `h2-mariadb-functions`

<details>

<summary>List of supported functions</summary>

* [`ADDDATE`](https://mariadb.com/kb/en/adddate/)
* [`ADDTIME`](https://mariadb.com/kb/en/addtime/)
* [`ADD_MONTHS`](https://mariadb.com/kb/en/add_months/)
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
* [`PERIOD_DIFF`](https://mariadb.com/kb/en/period_add/)
* [`SEC_TO_TIME`](https://mariadb.com/kb/en/sec_to_time/)
* [`STR_TO_DATE`](https://mariadb.com/kb/en/str_to_date/)
* [`SUBDATE`](https://mariadb.com/kb/en/subdate/)
* [`SUBTIME`](https://mariadb.com/kb/en/subtime/)
* [`TIME`](https://mariadb.com/kb/en/time-function/)
* [`TIMEDIFF`](https://mariadb.com/kb/en/timediff/)
* [`TIMESTAMP`](https://mariadb.com/kb/en/timestamp-function/)
* [`TIME_FORMAT`](https://mariadb.com/kb/en/time_format/)
* [`TIME_TO_SEC`](https://mariadb.com/kb/en/time_to_sec/)
* [`TO_DAYS`](https://mariadb.com/kb/en/to_days/)
* [`TO_SECONDS`](https://mariadb.com/kb/en/to_seconds/)
* [`UTC_DATE`](https://mariadb.com/kb/en/utc_date/)
* [`UTC_TIME`](https://mariadb.com/kb/en/utc_time/)
* [`UTC_TIME`](https://mariadb.com/kb/en/utc_time/)
* [`WEEKDAY`](https://mariadb.com/kb/en/weekday/)
* [`WEEKOFYEAR`](https://mariadb.com/kb/en/weekofyear/)
* [`YEARWEEK`](https://mariadb.com/kb/en/yearweek/)

</details>

#### IP address functions

Module `h2-mariadb-inet`

<details>

<summary>List of supported functions</summary>

* [`INET_ATON`](https://mariadb.com/kb/en/inet_aton/)
* [`INET_NTOA`](https://mariadb.com/kb/en/inet_ntoa/)
* [`INET6_ATON`](https://mariadb.com/kb/en/inet6_aton/)
* [`INET6_NTOA`](https://mariadb.com/kb/en/inet6_ntoa/)
* [`IS_IPV4`](https://mariadb.com/kb/en/is_ipv4/)
* [`IS_IPV4_COMPAT`](https://mariadb.com/kb/en/is_ipv4_compat/)
* [`IS_IPV4_MAPPED`](https://mariadb.com/kb/en/is_ipv4_mapped/)
* [`IS_IPV6`](https://mariadb.com/kb/en/is_ipv6/)

</details>

##### Notes

Due to how `DATE`, `TIME` and `TIMESTAMP` types in H2 works, the functions behave as if MariaDB had
[`ALLOW_INVALID_DATES`](https://mariadb.com/kb/en/sql-mode/#allow_invalid_dates), [`NO_ZERO_DATE`](https://mariadb.com/kb/en/sql-mode/#no_zero_date) and [`NO_ZERO_IN_DATE`](https://mariadb.com/kb/en/sql-mode/#no_zero_in_date) SQL modes disabled.

## License

[MIT](https://choosealicense.com/licenses/mit/)

