package cz.miou.h2.mariadb.misc;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class FormatBytesTest {
    
    @ParameterizedTest
    @CsvSource({
        "1000, '1000 bytes'",
        "1024, '1.00 KiB'",
        "1000000, '976.56 KiB'",
        "1048576, '1.00 MiB'",
        "1000000000, '953.67 MiB'",
        "1073741874, '1.00 GiB'",
        "1000000000000, '931.32 GiB'",
        "1099511627776, '1.00 TiB'",
        "1000000000000000, '909.49 TiB'",
        "1125899906842624, '1.00 PiB'",
    })
    void testFormatBytes(long value, String expected) throws SQLException {
        verifyQuery(
            "SELECT FORMAT_BYTES(?)",
            st -> st.setLong(1, value),
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }
    
}