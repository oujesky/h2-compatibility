package cz.miou.h2.mariadb.string;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class FormatTest {

    @ParameterizedTest
    @CsvSource({
        "1234567890.09876543210, 4, '1,234,567,890.0988'",
        "1234567.89, 4, '1,234,567.8900'",
        "1234567.89, 0, '1,234,568'",
    })
    void testFormat(BigDecimal input, int decimals, String expected) throws SQLException {
        verifyQuery(
            "SELECT FORMAT(?, ?)",
            st -> {
                st.setBigDecimal(1, input);
                st.setInt(2, decimals);
            },
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "123456789, 2, 'rm_CH', '123’456’789.00'",
        "1234567890.09876543210, 4, 'cs_CZ', '1\u00A0234\u00A0567\u00A0890,0988'",
    })
    void testFormatWithLocale(BigDecimal input, int decimals, String locale, String expected) throws SQLException {
        verifyQuery(
            "SELECT FORMAT(?, ?, ?)",
            st -> {
                st.setBigDecimal(1, input);
                st.setInt(2, decimals);
                st.setString(3, locale);
            },
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }


}