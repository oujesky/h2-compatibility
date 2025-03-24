package cz.miou.h2.mariadb.string;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class NaturalSortKeyTest {

    @ParameterizedTest
    @CsvSource(delimiter = '\t', value = {
        // from https://github.com/MariaDB/server/blob/main/mysql-test/main/natural_sort_key.result
        "0                           	00",
        "1                           	01",
        "2                           	02",
        "3                           	03",
        "4                           	04",
        "5                           	05",
        "6                           	06",
        "7                           	07",
        "8                           	08",
        "9                           	09",
        "10                          	110",
        "100                         	2100",
        "1000                        	31000",
        "10000                       	410000",
        "100000                      	5100000",
        "1000000                     	61000000",
        "10000000                    	710000000",
        "100000000                   	8100000000",
        "1000000000                  	901000000000",
        "10000000000                 	9110000000000",
        "100000000000                	92100000000000",
        "1000000000000               	931000000000000",
        "10000000000000              	9410000000000000",
        "100000000000000             	95100000000000000",
        "1000000000000000            	961000000000000000",
        "10000000000000000           	9710000000000000000",
        "100000000000000000          	98100000000000000000",
        "1000000000000000000         	9901000000000000000000",
        "10000000000000000000        	99110000000000000000000",
        "100000000000000000000       	992100000000000000000000",
        "1000000000000000000000      	9931000000000000000000000",
        "10000000000000000000000     	99410000000000000000000000",
        "100000000000000000000000    	995100000000000000000000000",
        "1000000000000000000000000   	9961000000000000000000000000",
        "10000000000000000000000000  	99710000000000000000000000000",
        "100000000000000000000000000 	998100000000000000000000000000",
        "1000000000000000000000000000	99901271000000000000000000000000000",
        "1-02                	01-02",
        "1-2                 	01-02",
        "1-20                	01-120",
        "10-20               	110-120",
        "100.8.9.0           	2100.08.09.00",
        "100.50.60.70        	2100.150.160.170",
        "100.200.300.400     	2100.2200.2300.2400",
        "1999-3-3            	31999-03-03",
        "1999-12-25          	31999-112-125",
        "2000-1-2            	32000-01-02",
        "2000-1-10           	32000-01-110",
        "2000-3-23           	32000-03-123",
        "a1b1                	a01b01",
        "a01b2               	a01b02",
        "a1b2                	a01b02",
        "a01b3               	a01b03",
        "fred                	fred",
        "jane                	jane",
        "pic   7             	pic   07",
        "pic 4 else          	pic 04 else",
        "pic 5               	pic 05",
        "pic 5 something     	pic 05 something",
        "pic 6               	pic 06",
        "pic01               	pic01",
        "pic02               	pic02",
        "pic2                	pic02",
        "pic02a              	pic02a",
        "pic3                	pic03",
        "pic4                	pic04",
        "pic05               	pic05",
        "pic100              	pic2100",
        "pic100a             	pic2100a",
        "pic120              	pic2120",
        "pic121              	pic2121",
        "pic02000            	pic32000",
        "tom                 	tom",
    })
    void testNaturalSortKey(String input, String expected) throws SQLException {
        verifyQuery(
            "SELECT NATURAL_SORT_KEY(?)",
            st -> st.setString(1, input),
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

}
