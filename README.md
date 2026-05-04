# h2-compatibility

Library of functions to extend [H2](https://www.h2database.com/) database with functions missing in compatibility modes.

Mainly for simplified integration testing in cases where running a real target database in container or standalone 
could be considered an overkill.

Aim is to provide missing functions, aggregations and types as compatible as possible, with minimal additional 
dependencies and targeting same minimal JDK version as H2 project, which is currently Java 11. 
Minimum H2 version that is compatible is 2.2.x.

Functions are split into modules to minimize dependency footprint and are dynamically registered 
using the Java `ServiceLoader` mechanism.

### Disclaimer

Though the functions have a suite of tests, there might be differences to original DB functions and 
these are not optimized performance-wise - that means this library is intended for test usage and is not 
recommended for any production or mission-critical deployment.

## Usage

### 1. Include required modules

#### Gradle
```kotlin
testRuntimeOnly("cz.miou.h2:h2-[DB]-[FUNCTIONS]:1.1.0")
```

#### Maven

```xml
<dependency>
    <groupId>cz.miou.h2</groupId>
    <artifactId>h2-[DB]-[FUNCTIONS]</artifactId>
    <version>1.1.0</version>
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

#### Using Flyway migration

As a convenience option for projects using [Flyway](https://github.com/flyway/flyway) for handling database migrates, 
it's possible to add a dependency to `h2-loader-flyway` module, that will automatically provide a migration with 
version 0 targeting a H2 database environment, that would load all currently present functions.  

##### Gradle
```kotlin
testRuntimeOnly("cz.miou.h2:h2-loader-flyway:1.1.0")
```

##### Maven

```xml
<dependency>
    <groupId>cz.miou.h2</groupId>
    <artifactId>h2-loader-flyway</artifactId>
    <version>1.1.0</version>
    <scope>test</scope>
</dependency>
```

#### Troubleshooting

If for some reason some of the functions aren't compatible with H2 anymore, it's possible to exclude them by specifying `cz.miou.h2.loader.exclude` system property with a coma separated list of function names.

## Supported Functions

### MariaDB / MySQL

#### Gradle
```kotlin
testRuntimeOnly("cz.miou.h2:h2-mariadb-all:1.1.0")
```

#### Maven

```xml
<dependency>
    <groupId>cz.miou.h2</groupId>
    <artifactId>h2-mariadb-all</artifactId>
    <version>1.1.0</version>
    <scope>test</scope>
</dependency>
```

In case a stricter control over dependencies is needed, it's possible to include only as specific module.

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
* [`BIT_XOR`](https://mariadb.com/kb/en/bit_xor/)
* [`CHARSET`](https://mariadb.com/kb/en/charset/)
* [`COERCIBILITY`](https://mariadb.com/kb/en/coercibility/)
* [`COLLATION`](https://mariadb.com/kb/en/collation/)
* [`CONV`](https://mariadb.com/kb/en/conv/)
* [`CONNECTION_ID`](https://mariadb.com/kb/en/connection_id/)
* [`FORMAT_BYTES`](https://mariadb.com/kb/en/miscellaneous-functions-format_bytes/)
* [`ISNULL`](https://mariadb.com/kb/en/isnull/)
* [`LOG2`](https://mariadb.com/kb/en/log2/)
* [`OCT`](https://mariadb.com/kb/en/oct/)
* [`POW`](https://mariadb.com/kb/en/pow/)
* [`SLEEP`](https://mariadb.com/kb/en/sleep/)
* [`STD`](https://mariadb.com/kb/en/std/)
* [`SYS_GUID`](https://mariadb.com/kb/en/sys_guid/)
* [`TO_NUMBER`](https://mariadb.com/kb/en/to_number/)
* [`UNCOMPRESSED_LENGTH`](https://mariadb.com/kb/en/uncompressed_length/)
* [`UNCOMPRESS`](https://mariadb.com/kb/en/uncompress/)
* [`UUID_SHORT`](https://mariadb.com/kb/en/uuid_short/)
* [`VERSION`](https://mariadb.com/kb/en/version/)

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

[`GET_FORMAT`](https://mariadb.com/kb/en/get_format/) is intentionally left out because MariaDB requires a keyword
first argument, for example `GET_FORMAT(DATE, 'EUR')` which is not possible to replicate with H2.

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

#### Hashing and cryptography functions

Module `h2-mariadb-crypto`

<details>

<summary>List of supported functions</summary>

* [`AES_DECRYPT`](https://mariadb.com/kb/en/aes_decrypt/)
* [`AES_ENCRYPT`](https://mariadb.com/kb/en/aes_encrypt/)
* [`CRC32C`](https://mariadb.com/kb/en/crc32c/)
* [`CRC32`](https://mariadb.com/kb/en/crc32/)
* [`KDF`](https://mariadb.com/kb/en/kdf/)
* [`MD5`](https://mariadb.com/kb/en/md5/)
* [`SHA1`](https://mariadb.com/kb/en/sha1/)
* [`SHA2`](https://mariadb.com/kb/en/sha2/)

</details>

#### JSON functions

Module `h2-mariadb-json`

<details>

<summary>List of supported functions</summary>

* [`JSON_ARRAY_APPEND`](https://mariadb.com/kb/en/json_array_append/)
* [`JSON_ARRAY_INSERT`](https://mariadb.com/kb/en/json_array_insert/)
* [`JSON_ARRAY_INTERSECT`](https://mariadb.com/kb/en/json_array_intersect/)
* [`JSON_COMPACT`](https://mariadb.com/kb/en/json_compact/)
* [`JSON_CONTAINS`](https://mariadb.com/kb/en/json_contains/)
* [`JSON_CONTAINS_PATH`](https://mariadb.com/kb/en/json_contains_path/)
* [`JSON_DEPTH`](https://mariadb.com/kb/en/json_depth/)
* [`JSON_DETAILED`](https://mariadb.com/kb/en/json_detailed/)
* [`JSON_EQUALS`](https://mariadb.com/kb/en/json_equals/)
* [`JSON_EXISTS`](https://mariadb.com/kb/en/json_exists/)
* [`JSON_EXTRACT`](https://mariadb.com/kb/en/json_extract/)
* [`JSON_INSERT`](https://mariadb.com/kb/en/json_insert/)
* [`JSON_KEYS`](https://mariadb.com/kb/en/json_keys/)
* [`JSON_KEY_VALUE`](https://mariadb.com/kb/en/json_key_value/)
* [`JSON_LENGTH`](https://mariadb.com/kb/en/json_length/)
* [`JSON_LOOSE`](https://mariadb.com/kb/en/json_loose/)
* [`JSON_MERGE`](https://mariadb.com/kb/en/json_merge/)
* [`JSON_MERGE_PATCH`](https://mariadb.com/kb/en/json_merge_patch/)
* [`JSON_NORMALIZE`](https://mariadb.com/kb/en/json_normalize/)
* [`JSON_OBJECT_FILTER_KEYS`](https://mariadb.com/kb/en/json_object_filter_keys/)
* [`JSON_OBJECT_TO_ARRAY`](https://mariadb.com/kb/en/json_object_to_array/)
* [`JSON_OVERLAPS`](https://mariadb.com/kb/en/json_overlaps/)
* [`JSON_QUERY`](https://mariadb.com/kb/en/json_query/)
* [`JSON_QUOTE`](https://mariadb.com/kb/en/json_quote/)
* [`JSON_REMOVE`](https://mariadb.com/kb/en/json_remove/)
* [`JSON_REPLACE`](https://mariadb.com/kb/en/json_replace/)
* [`JSON_SEARCH`](https://mariadb.com/kb/en/json_search/)
* [`JSON_SCHEMA_VALID`](https://mariadb.com/kb/en/json_schema_valid/)
* [`JSON_SET`](https://mariadb.com/kb/en/json_set/)
* [`JSON_TYPE`](https://mariadb.com/kb/en/json_type/)
* [`JSON_UNQUOTE`](https://mariadb.com/kb/en/json_unquote/)
* [`JSON_VALID`](https://mariadb.com/kb/en/json_valid/)
* [`JSON_VALUE`](https://mariadb.com/kb/en/json_value/)

</details>

#### UUID functions

Module `h2-mariadb-uuid`

<details>

<summary>List of supported functions</summary>

* [`UUIDV4`](https://mariadb.com/kb/en/uuidv4/)
* [`UUIDV7`](https://mariadb.com/kb/en/uuidv7/)

</details>

#### Spatial functions

Module `h2-mariadb-spatial`

<details>

<summary>List of supported types</summary>

* [`POINT`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/point)
* [`LINESTRING`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/linestring) (implemented in H2GIS)
* [`POLYGON`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/polygon)
* [`MULTIPOINT`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/multipoint)
* [`MULTILINESTRING`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/multilinestring)
* [`MULTIPOLYGON`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/multipolygon)
* [`GEOMETRYCOLLECTION`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/geometrycollection)

</details>

<details>

<summary>List of supported functions</summary>

* [`AREA`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/polygon-properties/polygon-properties-area) (implemented in H2GIS)
* [`AsBinary`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkb/wkb-asbinary) (implemented in H2GIS)
* [`AsText`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkt/wkt-astext) (implemented in H2GIS)
* [`AsWKB`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkb/aswkb) (implemented in H2GIS)
* [`AsWKT`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkt/wkt-aswkt) (implemented in H2GIS)
* [`BOUNDARY`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-properties/geometry-properties-boundary) (implemented in H2GIS)
* [`BUFFER`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/buffer) (implemented in H2GIS)
* [`CENTROID`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/polygon-properties/centroid) (implemented in H2GIS)
* [`CONTAINS`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-relations/contains) (implemented in H2GIS)
* [`CONVEXHULL`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/convexhull) (implemented in H2GIS)
* [`CROSSES`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-relations/crosses) (implemented in H2GIS)
* [`DIMENSION`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-properties/dimension) (implemented in H2GIS)
* [`DISJOINT`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-relations/disjoint) (implemented in H2GIS)
* [`ENDPOINT`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/linestring-properties/linestring-properties-endpoint) (implemented in H2GIS)
* [`EQUALS`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-relations/equals) (implemented in H2GIS)
* [`EXTERIORRING`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/polygon-properties/polygon-properties-exteriorring) (implemented in H2GIS)
* [`GEOMETRYCOLLECTION`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/geometrycollection)
* [`GeomCollFromText`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkt/wkt-geomcollfromtext) (implemented in H2GIS)
* [`GeomCollFromWKB`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkb/wkb-geomcollfromwkb) (implemented in H2GIS)
* [`GeometryCollectionFromText`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkt/geometrycollectionfromtext) (implemented in H2GIS)
* [`GeometryCollectionFromWKB`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkb/geometrycollectionfromwkb) (implemented in H2GIS)
* [`GeometryFromText`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkt/geometryfromtext) (implemented in H2GIS)
* [`GeometryFromWKB`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkb/geometryfromwkb) (implemented in H2GIS)
* [`GeometryN`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-properties/geometry-properties-geometryn) (implemented in H2GIS)
* [`GeometryType`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-properties/geometry-properties-geometrytype) (implemented in H2GIS)
* [`GeomFromText`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkt/wkt-geomfromtext) (implemented in H2GIS)
* [`GeomFromWKB`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkb/wkb-geomfromwkb) (implemented in H2GIS)
* [`GLENGTH`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/linestring-properties/glength) (implemented in H2GIS)
* [`INTERIORRINGN`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/polygon-properties/polygon-properties-interiorringn) (implemented in H2GIS)
* [`INTERSECTS`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-relations/intersects) (implemented in H2GIS)
* [`IsClosed`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-properties/isclosed) (implemented in H2GIS)
* [`IsEmpty`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-properties/geometry-properties-isempty) (implemented in H2GIS)
* [`IsRing`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-properties/isring) (implemented in H2GIS)
* [`IsSimple`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-properties/geometry-properties-issimple) (implemented in H2GIS)
* [`LINESTRING`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/linestring)
* [`LineFromText`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkt/wkt-linefromtext) (implemented in H2GIS)
* [`LineFromWKB`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkb/wkb-linefromwkb) (implemented in H2GIS)
* [`LineStringFromText`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkt/linestringfromtext) (implemented in H2GIS)
* [`LineStringFromWKB`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkb/linestringfromwkb) (implemented in H2GIS)
* [`MBRContains`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/mbr-minimum-bounding-rectangle/mbrcontains)
* [`MBRCoveredBy`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/mbr-minimum-bounding-rectangle/mbrcoveredby)
* [`MBRDisjoint`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/mbr-minimum-bounding-rectangle/mbrdisjoint)
* [`MBREqual`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/mbr-minimum-bounding-rectangle/mbrequal)
* [`MBREquals`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/mbr-minimum-bounding-rectangle/mbrequal)
* [`MBRIntersects`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/mbr-minimum-bounding-rectangle/mbrintersects)
* [`MBROverlaps`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/mbr-minimum-bounding-rectangle/mbroverlaps)
* [`MBRTouches`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/mbr-minimum-bounding-rectangle/mbrtouches)
* [`MBRWithin`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/mbr-minimum-bounding-rectangle/mbrwithin)
* [`MLineFromText`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkt/mlinefromtext) (implemented in H2GIS)
* [`MLineFromWKB`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkb/mlinefromwkb) (implemented in H2GIS)
* [`MPointFromText`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkt/mpointfromtext) (implemented in H2GIS)
* [`MPointFromWKB`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkb/mpointfromwkb) (implemented in H2GIS)
* [`MPolyFromText`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkt/mpolyfromtext) (implemented in H2GIS)
* [`MPolyFromWKB`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkb/mpolyfromwkb) (implemented in H2GIS)
* [`MULTILINESTRING`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/multilinestring)
* [`MultiLineStringFromText`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkt/multilinestringfromtext) (implemented in H2GIS)
* [`MultiLineStringFromWKB`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkb/multilinestringfromwkb) (implemented in H2GIS)
* [`MULTIPOINT`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/multipoint)
* [`MultiPointFromText`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkt/multipointfromtext) (implemented in H2GIS)
* [`MultiPointFromWKB`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkb/multipointfromwkb) (implemented in H2GIS)
* [`MULTIPOLYGON`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/multipolygon)
* [`MultiPolygonFromText`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkt/multipolygonfromtext) (implemented in H2GIS)
* [`MultiPolygonFromWKB`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkb/multipolygonfromwkb) (implemented in H2GIS)
* [`NumGeometries`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-properties/geometry-properties-numgeometries) (implemented in H2GIS)
* [`NumInteriorRings`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/polygon-properties/polygon-properties-numinteriorrings) (implemented in H2GIS)
* [`NumPoints`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/linestring-properties/linestring-properties-numpoints) (implemented in H2GIS)
* [`OVERLAPS`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-relations/overlaps) (implemented in H2GIS)
* [`POINT`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/point) (implemented in H2GIS)
* [`PointFromText`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkt/wkt-pointfromtext) (implemented in H2GIS)
* [`PointFromWKB`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkb/wkb-pointfromwkb) (implemented in H2GIS)
* [`PointN`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/linestring-properties/linestring-properties-pointn) (implemented in H2GIS)
* [`PointOnSurface`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/pointonsurface) (implemented in H2GIS)
* [`POLYGON`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/polygon)
* [`PolyFromText`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkt/wkt-polyfromtext) (implemented in H2GIS)
* [`PolyFromWKB`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkb/wkb-polyfromwkb) (implemented in H2GIS)
* [`PolygonFromText`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkt/polygonfromtext) (implemented in H2GIS)
* [`PolygonFromWKB`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkb/polygonfromwkb) (implemented in H2GIS)
* [`SRID`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-properties/geometry-properties-srid) (implemented in H2GIS)
* [`STARTPOINT`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/linestring-properties/linestring-properties-startpoint) (implemented in H2GIS)
* [`ST_AREA`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/polygon-properties/st_area) (implemented in H2GIS)
* [`ST_AsBinary`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkb/st_asbinary) (implemented in H2GIS)
* [`ST_AsGeoJSON`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/geojson-st_asgeojson) (implemented in H2GIS)
* [`ST_AsText`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkt/st_astext) (implemented in H2GIS)
* [`ST_AsWKB`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkb/st_aswkb) (implemented in H2GIS)
* [`ST_AsWKT`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkt/st_aswkt) (implemented in H2GIS)
* [`ST_BOUNDARY`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-properties/st_boundary) (implemented in H2GIS)
* [`ST_BUFFER`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/st_buffer) (implemented in H2GIS)
* [`ST_CENTROID`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/polygon-properties/st_centroid) (implemented in H2GIS)
* [`ST_Collect`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/miscellaneous-gis-functions/st_collect) (implemented in H2GIS)
* [`ST_CONTAINS`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-relations/st-contains) (implemented in H2GIS)
* [`ST_CONVEXHULL`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/st_convexhull) (implemented in H2GIS)
* [`ST_CROSSES`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-relations/st-crosses) (implemented in H2GIS)
* [`ST_DIFFERENCE`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-relations/st_difference) (implemented in H2GIS)
* [`ST_DIMENSION`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-properties/st_dimension) (implemented in H2GIS)
* [`ST_DISJOINT`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-relations/st_disjoint) (implemented in H2GIS)
* [`ST_DISTANCE`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-relations/st_distance) (implemented in H2GIS)
* [`ST_DISTANCE_SPHERE`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-relations/st_distance_sphere)
* [`ST_ENDPOINT`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/linestring-properties/st_endpoint) (implemented in H2GIS)
* [`ST_ENVELOPE`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-properties/st_envelope) (implemented in H2GIS)
* [`ST_EQUALS`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-relations/st-equals) (implemented in H2GIS)
* [`ST_EXTERIORRING`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/polygon-properties/st_exteriorring) (implemented in H2GIS)
* [`ST_GeoHash`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/miscellaneous-gis-functions/st_geohash)
* [`ST_GeomCollFromText`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkt/st_geomcollfromtext) (implemented in H2GIS)
* [`ST_GeomCollFromWKB`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkb/st_geomcollfromwkb) (implemented in H2GIS)
* [`ST_GeomFromGeoJSON`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/st_geomfromgeojson) (implemented in H2GIS)
* [`ST_GeomFromText`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkt/st_geomfromtext) (implemented in H2GIS)
* [`ST_GeomFromWKB`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkb/st_geomfromwkb) (implemented in H2GIS)
* [`ST_GEOMETRYN`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-properties/st_geometryn) (implemented in H2GIS)
* [`ST_GEOMETRYTYPE`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-properties/st_geometrytype) (implemented in H2GIS)
* [`ST_GeometryCollectionFromText`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkt/st_geometrycollectionfromtext) (implemented in H2GIS)
* [`ST_GeometryCollectionFromWKB`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkb/st_geometrycollectionfromwkb) (implemented in H2GIS)
* [`ST_GeometryFromText`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkt/st_geometryfromtext) (implemented in H2GIS)
* [`ST_GeometryFromWKB`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkb/st_geometryfromwkb) (implemented in H2GIS)
* [`ST_INTERIORRINGN`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/polygon-properties/st_interiorringn) (implemented in H2GIS)
* [`ST_INTERSECTION`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/st_intersection) (implemented in H2GIS)
* [`ST_INTERSECTS`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-relations/st-intersects) (implemented in H2GIS)
* [`ST_ISCLOSED`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-properties/st_isclosed) (implemented in H2GIS)
* [`ST_ISEMPTY`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-properties/st_isempty) (implemented in H2GIS)
* [`ST_IsRing`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-properties/st_isring) (implemented in H2GIS)
* [`ST_IsSimple`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-properties/st_issimple) (implemented in H2GIS)
* [`ST_IsValid`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/miscellaneous-gis-functions/st_isvalid) (implemented in H2GIS)
* [`ST_LatFromGeoHash`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/miscellaneous-gis-functions/st_latfromgeohash)
* [`ST_LENGTH`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-relations/st_length) (implemented in H2GIS)
* [`ST_LineFromText`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkt/st_linefromtext) (implemented in H2GIS)
* [`ST_LineFromWKB`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkb/st_linefromwkb) (implemented in H2GIS)
* [`ST_LineStringFromText`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkt/st_linestringfromtext) (implemented in H2GIS)
* [`ST_LineStringFromWKB`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkb/st_linestringfromwkb) (implemented in H2GIS)
* [`ST_LongFromGeoHash`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/miscellaneous-gis-functions/st_longfromgeohash)
* [`ST_MLineFromText`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkt/mlinefromtext) (implemented in H2GIS)
* [`ST_MLineFromWKB`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkb/mlinefromwkb) (implemented in H2GIS)
* [`ST_MPointFromText`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkt/st_mpointfromtext) (implemented in H2GIS)
* [`ST_MPointFromWKB`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkb/st_mpointfromwkb) (implemented in H2GIS)
* [`ST_MPolyFromText`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkt/st_mpolyfromtext) (implemented in H2GIS)
* [`ST_MPolyFromWKB`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkb/st_mpolyfromwkb) (implemented in H2GIS)
* [`ST_MultiLineStringFromText`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkt/st_multilinestringfromtext) (implemented in H2GIS)
* [`ST_MultiLineStringFromWKB`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkb/multilinestringfromwkb) (implemented in H2GIS)
* [`ST_MultiPointFromText`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkt/st_multipointfromtext) (implemented in H2GIS)
* [`ST_MultiPointFromWKB`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkb/st_multipointfromwkb) (implemented in H2GIS)
* [`ST_MultiPolygonFromText`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkt/st_multipolygonfromtext) (implemented in H2GIS)
* [`ST_MultiPolygonFromWKB`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkb/st_multipolygonfromwkb) (implemented in H2GIS)
* [`ST_NUMGEOMETRIES`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-properties/st_numgeometries) (implemented in H2GIS)
* [`ST_NUMINTERIORRINGS`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/polygon-properties/st_numinteriorrings) (implemented in H2GIS)
* [`ST_NUMPOINTS`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/linestring-properties/st_numpoints) (implemented in H2GIS)
* [`ST_OVERLAPS`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-relations/st-overlaps) (implemented in H2GIS)
* [`ST_POINTN`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/linestring-properties/st_pointn) (implemented in H2GIS)
* [`ST_POINTONSURFACE`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/st_pointonsurface) (implemented in H2GIS)
* [`ST_PointFromGeoHash`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/miscellaneous-gis-functions/st_pointfromgeohash)
* [`ST_PointFromText`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkt/st_pointfromtext) (implemented in H2GIS)
* [`ST_PointFromWKB`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkb/st_pointfromwkb) (implemented in H2GIS)
* [`ST_PolyFromText`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkt/st_polyfromtext) (implemented in H2GIS)
* [`ST_PolyFromWKB`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkb/st_polyfromwkb) (implemented in H2GIS)
* [`ST_PolygonFromText`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkt/st_polygonfromtext) (implemented in H2GIS)
* [`ST_PolygonFromWKB`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/wkb/st_polygonfromwkb) (implemented in H2GIS)
* [`ST_RELATE`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-properties/st_relate) (implemented in H2GIS)
* [`ST_Simplify`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/miscellaneous-gis-functions/st_simplify) (implemented in H2GIS)
* [`ST_SRID`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-properties/st_srid) (implemented in H2GIS)
* [`ST_STARTPOINT`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/linestring-properties/st_startpoint) (implemented in H2GIS)
* [`ST_SYMDIFFERENCE`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/st_symdifference) (implemented in H2GIS)
* [`ST_TOUCHES`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-relations/st-touches) (implemented in H2GIS)
* [`ST_UNION`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/st_union) (implemented in H2GIS)
* [`ST_Validate`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/miscellaneous-gis-functions/st_validate)
* [`ST_WITHIN`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-relations/st-within) (implemented in H2GIS)
* [`ST_X`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/point-properties/st_x) (implemented in H2GIS)
* [`ST_Y`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/point-properties/st_y) (implemented in H2GIS)
* [`TOUCHES`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-relations/touches) (implemented in H2GIS)
* [`WITHIN`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-relations/within) (implemented in H2GIS)
* [`X`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/point-properties/point-properties-x) (implemented in H2GIS)
* [`Y`](https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/point-properties/point-properties-y) (implemented in H2GIS)

`ENVELOPE` is not supported because it collides with H2's built-in `ENVELOPE` aggregate function.

</details>

##### Notes

Due to how `DATE`, `TIME` and `TIMESTAMP` types in H2 works, the functions behave as if MariaDB had
[`ALLOW_INVALID_DATES`](https://mariadb.com/kb/en/sql-mode/#allow_invalid_dates), [`NO_ZERO_DATE`](https://mariadb.com/kb/en/sql-mode/#no_zero_date) and [`NO_ZERO_IN_DATE`](https://mariadb.com/kb/en/sql-mode/#no_zero_in_date) SQL modes disabled.

## License

[MIT](https://choosealicense.com/licenses/mit/)
