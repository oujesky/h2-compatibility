package cz.miou.h2.mariadb.crypto;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class AesEncryptTest {
    
    @ParameterizedTest
    @CsvSource({
        "'foo', 'bar', , 'aes-128-ecb', '93f254924801b8b0f000571dfd8c4a5e'",
        "'foo', 'bar', , 'aes-192-ecb', '8ad939057257dc338f89f7ff48e913fa'",
        "'foo', 'bar', , 'aes-256-ecb', '63e24b363ac602838d30fc4f798fca23'",
        "'foo', 'bar', '0123456789abcdef', 'aes-128-cbc', '853e353a3eaf4f670ff5e568e66d126f'",
        "'foo', 'bar', '0123456789abcdef', 'aes-192-cbc', 'dc14fc86d2ec6b42413c70ea0ab1b0ae'",
        "'foo', 'bar', '0123456789abcdef', 'aes-256-cbc', '42a3eb91e6dfc40a900d278f99e0726e'",
        "'foo', 'bar', '0123456789abcdef', 'aes-128-ctr', 'c57c4b'",
        "'foo', 'bar', '0123456789abcdef', 'aes-192-ctr', '9ad569'",
        "'foo', 'bar', '0123456789abcdef', 'aes-256-ctr', 'b6cb0e'",
    })
    void testAesEncrypt(String str, String key, String iv, String mode, String expected) throws SQLException {
        verifyQuery(
            "SELECT RAWTOHEX(AES_ENCRYPT(?, ?, ?, ?))",
            st -> {
                st.setString(1, str);
                st.setString(2, key);
                st.setString(3, iv);
                st.setString(4, mode);
            },
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "'foo', 'bar', '93f254924801b8b0f000571dfd8c4a5e'",
    })
    void testAesEncryptDefaultMode(String str, String key, String expected) throws SQLException {
        verifyQuery(
            "SELECT RAWTOHEX(AES_ENCRYPT(?, ?))",
            st -> {
                st.setString(1, str);
                st.setString(2, key);
            },
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }
    
}