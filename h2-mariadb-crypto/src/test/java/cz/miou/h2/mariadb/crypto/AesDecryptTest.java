package cz.miou.h2.mariadb.crypto;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class AesDecryptTest {

    @ParameterizedTest
    @CsvSource({
        "0x93f254924801b8b0f000571dfd8c4a5e, 'bar', , 'aes-128-ecb', 'foo'",
        "0x8ad939057257dc338f89f7ff48e913fa, 'bar', , 'aes-192-ecb', 'foo'",
        "0x63e24b363ac602838d30fc4f798fca23, 'bar', , 'aes-256-ecb', 'foo'",
        "0x853e353a3eaf4f670ff5e568e66d126f, 'bar', '0123456789abcdef', 'aes-128-cbc', 'foo'",
        "0xdc14fc86d2ec6b42413c70ea0ab1b0ae, 'bar', '0123456789abcdef', 'aes-192-cbc', 'foo'",
        "0x42a3eb91e6dfc40a900d278f99e0726e, 'bar', '0123456789abcdef', 'aes-256-cbc', 'foo'",
        "0xc57c4b, 'bar', '0123456789abcdef', 'aes-128-ctr', 'foo'",
        "0x9ad569, 'bar', '0123456789abcdef', 'aes-192-ctr', 'foo'",
        "0xb6cb0e, 'bar', '0123456789abcdef', 'aes-256-ctr', 'foo'",
    })
    void testAesDecrypt(String str, String key, String iv, String mode, String expected) throws SQLException {
        verifyQuery(
            String.format("SELECT AES_DECRYPT(%s, ?, ?, ?)", str),
            st -> {
                st.setString(1, key);
                st.setString(2, iv);
                st.setString(3, mode);
            },
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "0x93f254924801b8b0f000571dfd8c4a5e, 'bar', 'foo'",
    })
    void testAesEncryptDefaultMode(String str, String key, String expected) throws SQLException {
        verifyQuery(
            String.format("SELECT AES_DECRYPT(%s, ?)", str),
            st -> st.setString(1, key),
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }
}