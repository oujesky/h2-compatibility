package cz.miou.h2.mariadb.crypto;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class KdfTest {
    
    @ParameterizedTest
    @CsvSource({
        "'foo', 'bar', 'infa', 'hkdf', 128, '612875f859cfb4ee0dfeff9f2a18e836'",
        "'foo', 'bar', 'infa', 'hkdf', 256, '612875f859cfb4ee0dfeff9f2a18e836e014364f5c97ec55e3e3ee75f692fa8c'",
        "'foo', 'bar', '1000', 'pbkdf2_hmac', 128, '76ba6dec5c3f6a60704d730a2a4baa1c'",
        "'foo', 'bar', '1000', 'pbkdf2_hmac', 256, '76ba6dec5c3f6a60704d730a2a4baa1c596f278ae35408ee5d67ece0942c14f4'"
    })
    void testKdf(String key, String salt, String info, String name, int width, String expected) throws SQLException {
        verifyQuery(
            "SELECT RAWTOHEX(KDF(?, ?, ?, ?, ?))",
            st -> {
                st.setString(1, key);
                st.setString(2, salt);
                st.setString(3, info);
                st.setString(4, name);
                st.setInt(5, width);
            },
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "'foo', 'bar', '76ba6dec5c3f6a60704d730a2a4baa1c'",
    })
    void testAesEncryptDefaultMode(String str, String key, String expected) throws SQLException {
        verifyQuery(
            "SELECT RAWTOHEX(KDF(?, ?))",
            st -> {
                st.setString(1, str);
                st.setString(2, key);
            },
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }
    
}