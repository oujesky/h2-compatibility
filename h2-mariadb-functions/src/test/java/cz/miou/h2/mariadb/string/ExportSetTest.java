package cz.miou.h2.mariadb.string;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class ExportSetTest {

    @ParameterizedTest
    @CsvSource({
        "5, 'Y', 'N', ',' , 4, 'Y,N,Y,N'",
        "6, '1', '0', ',', 10, '0,1,1,0,0,0,0,0,0,0'",
        "9223372036854775807, '1', '0', '', 64, '1111111111111111111111111111111111111111111111111111111111111110'",
        "1, '1', '0', '', -1, '1000000000000000000000000000000000000000000000000000000000000000'"
    })
    void testExportSet(long bits, String on, String off, String separator, int numberOfBits, String expected) throws SQLException {
        verifyQuery(
            "SELECT EXPORT_SET(?, ?, ?, ?, ?)",
            st -> {
                st.setLong(1, bits);
                st.setString(2, on);
                st.setString(3, off);
                st.setString(4, separator);
                st.setInt(5, numberOfBits);
            },
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

    @Test
    void testExportSetDefaultNumberOfBits() throws SQLException {
        verifyQuery(
            "SELECT EXPORT_SET(?, ?, ?, ?)",
            st -> {
                st.setInt(1, 6);
                st.setString(2, "1");
                st.setString(3, "0");
                st.setString(4, "X");
            },
            rs -> assertThat(rs.getString(1)).isEqualTo("0X1X1X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0X0")
        );
    }

    @Test
    void testExportSetDefaultSeparator() throws SQLException {
        verifyQuery(
            "SELECT EXPORT_SET(?, ?, ?)",
            st -> {
                st.setInt(1, 6);
                st.setString(2, "1");
                st.setString(3, "0");
            },
            rs -> assertThat(rs.getString(1)).isEqualTo("0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0")
        );
    }

}